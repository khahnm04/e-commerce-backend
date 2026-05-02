package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.common.enums.UserType;
import com.khahnm04.ecommerce.dto.request.*;
import com.khahnm04.ecommerce.dto.response.*;
import com.khahnm04.ecommerce.entity.*;
import com.khahnm04.ecommerce.common.constant.TokenConstants;
import com.khahnm04.ecommerce.repository.auth.ForgotPasswordTokenRepository;
import com.khahnm04.ecommerce.repository.auth.OtpVerificationRepository;
import com.khahnm04.ecommerce.repository.auth.RedisTokenRepository;
import com.khahnm04.ecommerce.repository.user.CustomerRepository;
import com.khahnm04.ecommerce.repository.user.EmployeeRepository;
import com.khahnm04.ecommerce.security.CookieProperties;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.security.JwtProperties;
import com.khahnm04.ecommerce.service.AuthService;
import com.khahnm04.ecommerce.service.CustomerService;
import com.khahnm04.ecommerce.service.EmailService;
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
import java.security.SecureRandom;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RedisTokenRepository redisTokenRepository;
    private final OtpVerificationRepository otpVerificationRepository;
    private final ForgotPasswordTokenRepository forgotPasswordTokenRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;
    private final CustomerService customerService;
    private final EmailService emailService;
    private final CookieProperties cookieProperties;
    private final JwtProperties jwtProperties;
    private final PasswordEncoder passwordEncoder;

    @Qualifier("adminAuthManager")
    private final AuthenticationManager adminAuthManager;

    @Qualifier("clientAuthManager")
    private final AuthenticationManager clientAuthManager;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int OTP_MAX_ATTEMPTS = 5;

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
                .isEmailVerified(customer.getIsEmailVerified())
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

        if (request.newPassword().equals(request.currentPassword())) {
            throw new AppException(ErrorCode.NEW_PASSWORD_SAME_AS_OLD);
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

        if (request.newPassword().equals(request.currentPassword())) {
            throw new AppException(ErrorCode.NEW_PASSWORD_SAME_AS_OLD);
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

    @Override
    @Transactional
    public void sendOtp(SendOtpRequest request) {
        String email = request.email();

        customerRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));

        String otp = generateOtp();

        OtpVerification otpVerification = OtpVerification.builder()
                .email(email)
                .otp(otp)
                .attempts(0)
                .ttl(300L)
                .build();
        otpVerificationRepository.save(otpVerification);

        emailService.sendOtpEmail(email, otp);
        log.info("OTP sent to email: {}", email);
    }

    @Override
    @Transactional
    public void verifyOtp(VerifyOtpRequest request) {
        String email = request.email();
        validateOtp(email, request.otp());

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));
        customer.setIsEmailVerified(true);
        customerRepository.save(customer);

        otpVerificationRepository.deleteById(email);
        log.info("Email verified successfully for: {}", email);
    }

    @Override
    @Transactional
    public ResetTokenResponse forgotPasswordVerifyOtp(ForgotPasswordVerifyOtpRequest request) {
        String email = request.email();
        validateOtp(email, request.otp());

        otpVerificationRepository.deleteById(email);

        String resetToken = UUID.randomUUID().toString();
        ForgotPasswordToken tokenEntity = ForgotPasswordToken.builder()
                .email(email)
                .token(resetToken)
                .expirationTime(600L) // 10 minutes
                .build();
        forgotPasswordTokenRepository.save(tokenEntity);

        log.info("Forgot Password OTP verified for email: {}", email);
        return ResetTokenResponse.builder().resetToken(resetToken).build();
    }

    @Override
    @Transactional
    public void forgotPasswordReset(ForgotPasswordResetRequest request) {
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_CONFIRMATION_MISMATCH);
        }

        String email = request.email();
        ForgotPasswordToken tokenEntity = forgotPasswordTokenRepository.findById(email)
                .orElseThrow(() -> new AppException(ErrorCode.RESET_TOKEN_INVALID));
        
        if (!tokenEntity.getToken().equals(request.resetToken())) {
            throw new AppException(ErrorCode.RESET_TOKEN_INVALID);
        }

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));

        customer.setPassword(passwordEncoder.encode(request.newPassword()));
        customerRepository.save(customer);
        
        forgotPasswordTokenRepository.deleteById(email);
        log.info("Password successfully reset for customer: {}", email);
    }

    // ======================== Private methods ========================

    private void validateOtp(String email, String otp) {
        OtpVerification otpVerification = otpVerificationRepository.findById(email)
                .orElseThrow(() -> new AppException(ErrorCode.OTP_EXPIRED));

        if (otpVerification.getAttempts() >= OTP_MAX_ATTEMPTS) {
            otpVerificationRepository.deleteById(email);
            throw new AppException(ErrorCode.OTP_MAX_ATTEMPTS);
        }

        if (!otpVerification.getOtp().equals(otp)) {
            otpVerification.setAttempts(otpVerification.getAttempts() + 1);
            otpVerificationRepository.save(otpVerification);
            throw new AppException(ErrorCode.OTP_INVALID);
        }
    }

    private String generateOtp() {
        int otp = 100000 + SECURE_RANDOM.nextInt(900000);
        return String.valueOf(otp);
    }

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
