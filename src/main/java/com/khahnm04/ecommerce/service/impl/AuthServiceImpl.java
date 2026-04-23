package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.entity.Employee;
import com.khahnm04.ecommerce.entity.User;
import com.khahnm04.ecommerce.repository.EmployeeRepository;
import com.khahnm04.ecommerce.repository.UserRepository;
import com.khahnm04.ecommerce.common.constant.TokenConstants;
import com.khahnm04.ecommerce.security.CookieProperties;
import com.khahnm04.ecommerce.dto.request.ChangePasswordRequest;
import com.khahnm04.ecommerce.dto.request.LoginRequest;
import com.khahnm04.ecommerce.dto.request.ResetPasswordRequest;
import com.khahnm04.ecommerce.dto.response.LoginResponse;
import com.khahnm04.ecommerce.dto.response.IntrospectResponse;
import com.khahnm04.ecommerce.dto.response.TokenPayload;
import com.khahnm04.ecommerce.entity.RedisToken;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.repository.RedisTokenRepository;
import com.khahnm04.ecommerce.security.JwtProperties;
import com.khahnm04.ecommerce.service.AuthService;
import com.khahnm04.ecommerce.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RedisTokenRepository redisTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmployeeRepository employeeRepository;
    private final CookieProperties cookieProperties;
    private final JwtProperties jwtProperties;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request, HttpServletResponse response) {
        String identifier = StringUtils.hasText(request.email())
                ? request.email()
                : request.phoneNumber();
        if (!StringUtils.hasText(identifier)) {
            throw new AppException(ErrorCode.LOGIN_IDENTIFIER_REQUIRED);
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(identifier, request.password()));

        User user = (User) authentication.getPrincipal();

        TokenPayload token = jwtService.generateToken(user, request.rememberMe());
        addTokenCookies(response, token.token(), request.rememberMe());

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("User with id = {} has been authenticated", user.getId());

        String jobTitle = employeeRepository.findByUserId(user.getId())
                .map(Employee::getJobTitle)
                .orElse(null);

        return LoginResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .fullName(user.getFullName())
                .avatar(user.getAvatar())
                .jobTitle(jobTitle)
                .token(token.token())
                .build();
    }

    @Override
    public void logout(String accessToken, HttpServletResponse response) {
        try {
            // save token to blacklist
            var signToken = jwtService.validateToken(accessToken);
            redisTokenRepository.save(RedisToken.builder()
                    .jwtId(signToken.getJWTClaimsSet().getJWTID())
                    .expiredTime(signToken.getJWTClaimsSet().getExpirationTime().getTime())
                    .build());
            log.info("save token successfully");

            // delete token from cookie
            clearTokenCookie(response);
            log.info("delete token from cookie successfully");

        } catch (AppException | ParseException e) {
            log.error("Token already expired");
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new AppException(ErrorCode.PASSWORD_CONFIRMATION_MISMATCH);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.NEW_PASSWORD_SAME_AS_OLD);
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        log.info("Changed password for user with id: {}", userId);
    }

    @Override
    @Transactional
    public void resetPassword(Long userId, ResetPasswordRequest request) {
        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new AppException(ErrorCode.PASSWORD_CONFIRMATION_MISMATCH);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        log.info("Reset password for user with id: {}", userId);
    }

    @Override
    public IntrospectResponse introspect(String token) {
        try {
            jwtService.validateToken(token);
            return IntrospectResponse.builder()
                    .valid(true)
                    .build();
        } catch (AppException e) {
            return IntrospectResponse.builder()
                    .valid(false)
                    .build();
        }
    }

    // ======================== Private methods ========================

    private void addTokenCookies(HttpServletResponse response, String token, boolean rememberMe) {
        ResponseCookie refreshCookie = ResponseCookie.from(TokenConstants.TOKEN, token)
                .httpOnly(cookieProperties.isHttpOnly())
                .secure(cookieProperties.isSecure())
                .domain(cookieProperties.getDomain())
                .path("/")
                .maxAge(rememberMe
                        ? jwtProperties.getTokenRememberExpiration()
                        : jwtProperties.getTokenExpiration())
                .sameSite(cookieProperties.getSameSite())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    public void clearTokenCookie(HttpServletResponse response) {
        ResponseCookie accessTokenCookie = ResponseCookie.from(TokenConstants.TOKEN, "")
                .maxAge(0)
                .domain(cookieProperties.getDomain())
                .path("/")
                .httpOnly(cookieProperties.isHttpOnly())
                .secure(cookieProperties.isSecure())
                .sameSite(cookieProperties.getSameSite())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
    }

}
