package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.common.enums.UserType;
import com.khahnm04.ecommerce.dto.response.IntrospectResponse;
import com.khahnm04.ecommerce.entity.Customer;
import com.khahnm04.ecommerce.entity.Employee;
import com.khahnm04.ecommerce.repository.CustomerRepository;
import com.khahnm04.ecommerce.repository.EmployeeRepository;
import com.khahnm04.ecommerce.common.constant.TokenConstants;
import com.khahnm04.ecommerce.security.CookieProperties;
import com.khahnm04.ecommerce.dto.request.ChangePasswordRequest;
import com.khahnm04.ecommerce.dto.request.LoginRequest;
import com.khahnm04.ecommerce.dto.request.ResetPasswordRequest;
import com.khahnm04.ecommerce.dto.response.LoginResponse;
import com.khahnm04.ecommerce.dto.request.RegisterRequest;
import com.khahnm04.ecommerce.dto.response.RegisterResponse;
import com.khahnm04.ecommerce.dto.response.TokenPayload;
import com.khahnm04.ecommerce.dto.request.CustomerCreationRequest;
import com.khahnm04.ecommerce.entity.RedisToken;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.repository.RedisTokenRepository;
import com.khahnm04.ecommerce.security.JwtProperties;
import com.khahnm04.ecommerce.service.AuthService;
import com.khahnm04.ecommerce.service.CustomerService;
import com.khahnm04.ecommerce.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RedisTokenRepository redisTokenRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;
    private final CustomerService customerService;
    private final CookieProperties cookieProperties;
    private final JwtProperties jwtProperties;
    private final PasswordEncoder passwordEncoder;

    @Qualifier("adminAuthManager")
    private final AuthenticationManager adminAuthManager;

    @Qualifier("clientAuthManager")
    private final AuthenticationManager clientAuthManager;

    @Override
    @Transactional
    public LoginResponse loginEmployee(LoginRequest request, HttpServletResponse response) {
        String identifier = StringUtils.hasText(request.email())
                ? request.email()
                : request.phoneNumber();

        if (!StringUtils.hasText(identifier)) {
            throw new AppException(ErrorCode.LOGIN_IDENTIFIER_REQUIRED);
        }

        Authentication authentication = adminAuthManager
                .authenticate(new UsernamePasswordAuthenticationToken(identifier, request.password()));

        Employee employee = (Employee) authentication.getPrincipal();

        String scope = buildEmployeeScope(employee);

        TokenPayload token = jwtService.generateToken(
                employee.getId(), getSubject(employee.getPhoneNumber(), employee.getEmail()),
                scope, UserType.EMPLOYEE, request.rememberMe());

        addTokenCookies(response, token.token(), request.rememberMe());

        employee.setLastLoginAt(LocalDateTime.now());
        employeeRepository.save(employee);

        log.info("Employee with id = {} has been authenticated", employee.getId());

        return LoginResponse.builder()
                .id(employee.getId())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .fullName(employee.getFullName())
                .avatar(employee.getAvatar())
                .jobTitle(employee.getJobTitle())
                .userType(UserType.EMPLOYEE.name())
                .token(token.token())
                .build();
    }

    @Override
    @Transactional
    public LoginResponse loginCustomer(LoginRequest request, HttpServletResponse response) {
        String identifier = StringUtils.hasText(request.email())
                ? request.email()
                : request.phoneNumber();

        if (!StringUtils.hasText(identifier)) {
            throw new AppException(ErrorCode.LOGIN_IDENTIFIER_REQUIRED);
        }

        Authentication authentication = clientAuthManager
                .authenticate(new UsernamePasswordAuthenticationToken(identifier, request.password()));

        Customer customer = (Customer) authentication.getPrincipal();

        TokenPayload token = jwtService.generateToken(
                customer.getId(), getSubject(customer.getPhoneNumber(), customer.getEmail()),
                "ROLE_CUSTOMER", UserType.CUSTOMER, request.rememberMe());

        addTokenCookies(response, token.token(), request.rememberMe());

        customer.setLastLoginAt(LocalDateTime.now());
        customerRepository.save(customer);

        log.info("Customer with id = {} has been authenticated", customer.getId());

        return LoginResponse.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .fullName(customer.getFullName())
                .avatar(customer.getAvatar())
                .userType(UserType.CUSTOMER.name())
                .token(token.token())
                .build();
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        CustomerCreationRequest customerRequest = CustomerCreationRequest.builder()
                .fullName(request.fullName())
                .phoneNumber(request.phoneNumber())
                .email(request.email())
                .password(request.password())
                .confirmPassword(request.confirmPassword())
                .dateOfBirth(request.dateOfBirth())
                .build();

        customerService.createCustomer(customerRequest);

        return RegisterResponse.builder()
                .fullName(request.fullName())
                .dateOfBirth(request.dateOfBirth())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .build();
    }

    @Override
    public void logout(String accessToken, HttpServletResponse response) {
        try {
            var signToken = jwtService.validateToken(accessToken);
            redisTokenRepository.save(RedisToken.builder()
                    .jwtId(signToken.getJWTClaimsSet().getJWTID())
                    .expiredTime(signToken.getJWTClaimsSet().getExpirationTime().getTime())
                    .build());
            log.info("save token successfully");

            clearTokenCookie(response);
            log.info("delete token from cookie successfully");

        } catch (AppException | ParseException e) {
            log.error("Token already expired");
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    @Override
    @Transactional
    public void changePasswordEmployee(Long employeeId, ChangePasswordRequest request) {
        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new AppException(ErrorCode.PASSWORD_CONFIRMATION_MISMATCH);
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        if (!passwordEncoder.matches(request.currentPassword(), employee.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        if (passwordEncoder.matches(request.newPassword(), employee.getPassword())) {
            throw new AppException(ErrorCode.NEW_PASSWORD_SAME_AS_OLD);
        }

        employee.setPassword(passwordEncoder.encode(request.newPassword()));
        employeeRepository.save(employee);
        log.info("Changed password for employee with id: {}", employeeId);
    }

    @Override
    @Transactional
    public void changePasswordCustomer(Long customerId, ChangePasswordRequest request) {
        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new AppException(ErrorCode.PASSWORD_CONFIRMATION_MISMATCH);
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));

        if (!passwordEncoder.matches(request.currentPassword(), customer.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        if (passwordEncoder.matches(request.newPassword(), customer.getPassword())) {
            throw new AppException(ErrorCode.NEW_PASSWORD_SAME_AS_OLD);
        }

        customer.setPassword(passwordEncoder.encode(request.newPassword()));
        customerRepository.save(customer);
        log.info("Changed password for customer with id: {}", customerId);
    }

    @Override
    @Transactional
    public void resetPasswordEmployee(Long employeeId, ResetPasswordRequest request) {
        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new AppException(ErrorCode.PASSWORD_CONFIRMATION_MISMATCH);
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        employee.setPassword(passwordEncoder.encode(request.newPassword()));
        employeeRepository.save(employee);
        log.info("Reset password for employee with id: {}", employeeId);
    }

    @Override
    @Transactional
    public void resetPasswordCustomer(Long customerId, ResetPasswordRequest request) {
        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new AppException(ErrorCode.PASSWORD_CONFIRMATION_MISMATCH);
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));

        customer.setPassword(passwordEncoder.encode(request.newPassword()));
        customerRepository.save(customer);
        log.info("Reset password for customer with id: {}", customerId);
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

    private String getSubject(String phoneNumber, String email) {
        return StringUtils.hasText(phoneNumber) ? phoneNumber : email;
    }

    private String buildEmployeeScope(Employee employee) {
        StringJoiner scope = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(employee.getRoles())) {
            employee.getRoles().forEach(role -> {
                scope.add("ROLE_" + role.getCode());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions()
                            .forEach(permission -> scope.add(permission.getCode()));
                }
            });
        }
        return scope.toString();
    }

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
