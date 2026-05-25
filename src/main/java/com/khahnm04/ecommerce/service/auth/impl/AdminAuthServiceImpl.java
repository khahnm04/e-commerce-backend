package com.khahnm04.ecommerce.service.auth.impl;

import com.khahnm04.ecommerce.dto.request.auth.AdminLoginRequest;
import com.khahnm04.ecommerce.dto.request.auth.LogoutRequest;
import com.khahnm04.ecommerce.dto.request.auth.RefreshTokenRequest;
import com.khahnm04.ecommerce.dto.request.auth.TokenRequest;
import com.khahnm04.ecommerce.dto.response.auth.AdminLoginResponse;
import com.khahnm04.ecommerce.dto.response.auth.IntrospectResponse;
import com.khahnm04.ecommerce.dto.response.auth.RefreshTokenResponse;
import com.khahnm04.ecommerce.dto.response.auth.TokenResponse;
import com.khahnm04.ecommerce.dto.response.user.EmployeeResponse;
import com.khahnm04.ecommerce.entity.auth.EmployeeRole;
import com.khahnm04.ecommerce.entity.auth.RedisToken;
import com.khahnm04.ecommerce.entity.auth.Role;
import com.khahnm04.ecommerce.constant.auth.UserType;
import com.khahnm04.ecommerce.entity.user.Employee;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.repository.auth.EmployeeRoleRepository;
import com.khahnm04.ecommerce.repository.auth.RedisTokenRepository;
import com.khahnm04.ecommerce.repository.auth.RolePermissionRepository;
import com.khahnm04.ecommerce.repository.user.EmployeeRepository;
import com.khahnm04.ecommerce.service.auth.AdminAuthService;
import com.khahnm04.ecommerce.service.auth.JwtService;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.security.core.Authentication;
import java.text.ParseException;
import java.time.Instant;
import java.util.List;
import java.util.StringJoiner;

@Slf4j(topic = "AUTH-SERVICE")
@Service
@RequiredArgsConstructor
public class AdminAuthServiceImpl implements AdminAuthService {

    private final JwtService jwtService;
    private final EmployeeRepository employeeRepository;
    private final RedisTokenRepository redisTokenRepository;
    private final EmployeeRoleRepository employeeRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Qualifier("adminAuthManager")
    private final AuthenticationManager adminAuthManager;

    @Override
    @Transactional
    public AdminLoginResponse login(AdminLoginRequest request) {
        Authentication authentication = adminAuthManager
            .authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        Employee employee = (Employee) authentication.getPrincipal();

        TokenRequest tokenRequest = TokenRequest.builder()
            .userId(employee.getId())
            .subject(employee.getEmail())
            .scope(buildScope(employee))
            .userType(UserType.EMPLOYEE)
            .rememberMe(request.rememberMe())
            .build();

        TokenResponse accessToken = jwtService.generateAccessToken(tokenRequest);
        TokenResponse refreshToken = jwtService.generateRefreshToken(tokenRequest);

        redisTokenRepository.save(RedisToken.builder()
            .jwtId(refreshToken.jwtId())
            .expiredTime(refreshToken.expiredTime().getTime() / 1000)
            .build());

        employee.setLastLoginAt(Instant.now());
        employeeRepository.save(employee);

        log.info("Employee with id = {} has been authenticated", employee.getId());
        return AdminLoginResponse.builder()
            .employee(EmployeeResponse.builder()
                .id(employee.getId())
                .jobTitle(employee.getJobTitle())
                .email(employee.getEmail())
                .fullName(employee.getFullName())
                .avatar(employee.getAvatar())
                .build())
            .userType(UserType.EMPLOYEE)
            .accessToken(accessToken.token())
            .refreshToken(refreshToken.token())
            .build();
    }

    @Override
    @Transactional
    public void logout(LogoutRequest request) {
        try {
            SignedJWT signRefreshToken = jwtService.verifyRefreshToken(request.refreshToken());
            redisTokenRepository.deleteById(signRefreshToken.getJWTClaimsSet().getJWTID());
            log.info("Remove refresh token successfully");
        } catch (ParseException e) {
            log.error("Cannot parse refresh token and logout failed");
            throw new AppException(ErrorCode.REFRESH_TOKEN_FAILED);
        }
    }

    @Override
    @Transactional
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        try {
            SignedJWT signedJWT = jwtService.verifyRefreshToken(request.refreshToken());
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();

            if (!redisTokenRepository.existsById(jwtId)) {
                throw new AppException(ErrorCode.REFRESH_TOKEN_FAILED);
            }

            String subject = signedJWT.getJWTClaimsSet().getSubject();
            Employee employee = employeeRepository
                .findByEmailAndDeletedAtIsNull(subject)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

            TokenRequest tokenRequest = TokenRequest.builder()
                .userId(employee.getId())
                .subject(employee.getEmail())
                .scope(buildScope(employee))
                .userType(UserType.EMPLOYEE)
                .build();

            TokenResponse newAccessToken = jwtService.generateAccessToken(tokenRequest);
            TokenResponse newRefreshToken = jwtService.generateRefreshToken(tokenRequest);

            redisTokenRepository.deleteById(jwtId);
            redisTokenRepository.save(RedisToken.builder()
                .jwtId(newRefreshToken.jwtId())
                .expiredTime(newRefreshToken.expiredTime().getTime() / 1000)
                .build());

            log.info("Refresh token success for employee: {}", subject);

            return RefreshTokenResponse.builder()
                .accessToken(newAccessToken.token())
                .refreshToken(newRefreshToken.token())
                .build();

        } catch (ParseException e) {
            log.error("Cannot parse refresh token");
            throw new AppException(ErrorCode.REFRESH_TOKEN_FAILED);
        }
    }

    @Override
    public IntrospectResponse introspect(String token) {
        try {
            jwtService.verifyAccessToken(token);
            return IntrospectResponse.builder()
                .valid(true)
                .build();
        } catch (AppException e) {
            return IntrospectResponse.builder()
                .valid(false)
                .build();
        }
    }

    private String buildScope(Employee employee) {
        List<Role> roles = employeeRoleRepository
            .findByEmployeeWithRole(employee)
            .stream()
            .map(EmployeeRole::getRole)
            .toList();
        if (CollectionUtils.isEmpty(roles)) {
            return "";
        }
        StringJoiner scope = new StringJoiner(" ");
        roles.forEach(role -> scope.add("ROLE_" + role.getCode()));
        return scope.toString();
    }

}
