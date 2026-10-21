package com.khahnm18.ecommercebackend.service.auth.impl;

import com.khahnm18.ecommercebackend.dto.request.auth.LoginRequest;
import com.khahnm18.ecommercebackend.dto.request.auth.RegisterRequest;
import com.khahnm18.ecommercebackend.dto.response.auth.LoginResponse;
import com.khahnm18.ecommercebackend.dto.request.auth.LogoutRequest;
import com.khahnm18.ecommercebackend.dto.request.auth.RefreshTokenRequest;
import com.khahnm18.ecommercebackend.dto.response.auth.RefreshTokenResponse;
import com.khahnm18.ecommercebackend.dto.response.auth.RegisterResponse;
import com.khahnm18.ecommercebackend.dto.response.auth.TokenResponse;
import com.khahnm18.ecommercebackend.entity.auth.Account;
import com.khahnm18.ecommercebackend.entity.auth.AccountRole;
import com.khahnm18.ecommercebackend.entity.auth.Role;
import com.khahnm18.ecommercebackend.entity.user.UserProfile;
import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.mapper.AccountMapper;
import com.khahnm18.ecommercebackend.mapper.UserMapper;
import com.khahnm18.ecommercebackend.repository.auth.AccountRepository;
import com.khahnm18.ecommercebackend.repository.auth.AccountRoleRepository;
import com.khahnm18.ecommercebackend.repository.auth.RoleRepository;
import com.khahnm18.ecommercebackend.repository.user.AdminProfileRepository;
import com.khahnm18.ecommercebackend.repository.user.UserProfileRepository;
import com.khahnm18.ecommercebackend.service.auth.AuthService;
import com.khahnm18.ecommercebackend.service.auth.JwtService;
import com.khahnm18.ecommercebackend.service.mail.MailService;
import com.khahnm18.ecommercebackend.service.redis.RedisService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.text.ParseException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.UUID;

import static com.khahnm18.ecommercebackend.constant.TokenConstant.WHITELIST_TOKEN;
import static com.khahnm18.ecommercebackend.constant.EmailSubjectConstant.WELCOME_SUBJECT;
import static com.khahnm18.ecommercebackend.constant.EmailTemplateConstant.WELCOME_TEMPLATE;

@Slf4j(topic = "AUTH-SERVICE")
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final AccountMapper accountMapper;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final RedisService redisService;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final AdminProfileRepository adminProfileRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public LoginResponse loginAdmin(LoginRequest request) {
        Account account = accountRepository.findByEmailAndIsDeletedFalse(request.email())
            .orElseThrow(() -> new AppException(ErrorCode.LOGIN_FAILED));

        if (!adminProfileRepository.existsByAccountIdAndIsDeletedFalse(account.getId())) {
            throw new AppException(ErrorCode.LOGIN_FAILED);
        }
        if (!passwordEncoder.matches(request.password(), account.getPassword())) {
            throw new AppException(ErrorCode.LOGIN_FAILED);
        }

        account.setLastLoginAt(Instant.now());
        accountRepository.save(account);

        TokenResponse accessToken = jwtService.generateAccessToken(account);
        TokenResponse refreshToken = jwtService.generateRefreshToken(account);

        saveRefreshTokenToRedis(refreshToken);

        log.info("Account admin with email: {} has been authenticated", account.getEmail());
        return LoginResponse.builder()
            .accountId(account.getId())
            .accessToken(accessToken.token())
            .refreshToken(refreshToken.token())
            .build();
    }

    @Override
    public LoginResponse loginClient(LoginRequest request) {
        Account account = accountRepository.findByEmailAndIsDeletedFalse(request.email())
            .orElseThrow(() -> new AppException(ErrorCode.LOGIN_FAILED));

        if (!userProfileRepository.existsByAccountIdAndIsDeletedFalse(account.getId())) {
            throw new AppException(ErrorCode.LOGIN_FAILED);
        }
        if (!passwordEncoder.matches(request.password(), account.getPassword())) {
            throw new AppException(ErrorCode.LOGIN_FAILED);
        }

        account.setLastLoginAt(Instant.now());
        accountRepository.save(account);

        TokenResponse accessToken = jwtService.generateAccessToken(account);
        TokenResponse refreshToken = jwtService.generateRefreshToken(account);

        saveRefreshTokenToRedis(refreshToken);

        log.info("Account client with email: {} has been authenticated", account.getEmail());
        return LoginResponse.builder()
            .accountId(account.getId())
            .accessToken(accessToken.token())
            .refreshToken(refreshToken.token())
            .build();
    }

    @Override
    public RegisterResponse registerClient(RegisterRequest request) {
        Account account = accountMapper.toAccount(request);
        account.setPassword(passwordEncoder.encode(request.password()));
        try {
            account = accountRepository.save(account);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Account save failed - duplicate entry: email={}", account.getEmail());
            throw new AppException(ErrorCode.ACCOUNT_EXISTED);
        }

        UserProfile userProfile = userMapper.toUserProfile(request, account.getId());
        userProfile = userProfileRepository.save(userProfile);

        Role customerRole = roleRepository.findByCodeAndIsDeletedFalse("CUSTOMER")
            .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        accountRoleRepository.save(AccountRole.builder()
            .accountId(account.getId())
            .roleId(customerRole.getId())
            .build());

        mailService.sendEmail(
            account.getEmail(),
            WELCOME_SUBJECT,
            WELCOME_TEMPLATE,
            Map.of("email", account.getEmail())
        );

        log.info("User registered: fullName={}, email={}", userProfile.getFullName(), account.getEmail());
        return RegisterResponse.builder()
            .fullName(userProfile.getFullName())
            .email(account.getEmail())
            .phoneNumber(account.getPhoneNumber())
            .build();
    }

    @Override
    public void logout(LogoutRequest request) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(request.refreshToken());
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            redisService.delete(WHITELIST_TOKEN + jwtId);
            log.info("Remove refresh_token from whitelist_token and logout successfully");
        } catch (ParseException e) {
            log.error("Cannot parse token and logout failed");
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        try {
            SignedJWT signedJWT = jwtService.verifyToken(request.refreshToken());
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();

            if (!redisService.hasKey(WHITELIST_TOKEN + jwtId)) {
                throw new AppException(ErrorCode.REFRESH_TOKEN_FAILED);
            }

            UUID accountId = UUID.fromString(signedJWT.getJWTClaimsSet().getSubject());

            Account account = accountRepository.findByIdAndIsDeletedFalse(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

            TokenResponse newAccessToken = jwtService.generateAccessToken(account);
            TokenResponse newRefreshToken = jwtService.generateRefreshToken(account);

            redisService.delete(WHITELIST_TOKEN + jwtId);
            saveRefreshTokenToRedis(newRefreshToken);

            log.info("Refresh token successfully, accountId: {}", accountId);
            return RefreshTokenResponse.builder()
                .accessToken(newAccessToken.token())
                .refreshToken(newRefreshToken.token())
                .build();
        } catch (ParseException e) {
            log.error("Refresh token failed!");
            throw new AppException(ErrorCode.REFRESH_TOKEN_FAILED);
        }
    }

    private void saveRefreshTokenToRedis(TokenResponse refreshToken) {
        long ttlSeconds = refreshToken
            .expiredTime()
            .getTime() / 1000 - Instant.now().getEpochSecond();

        redisService.setWithTTL(
            WHITELIST_TOKEN + refreshToken.jwtId(),
            refreshToken.jwtId(),
            ttlSeconds,
            TimeUnit.SECONDS
        );
    }

}
