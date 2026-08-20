package com.khahnm04.ecommerce.service.auth.impl;

import com.khahnm04.ecommerce.dto.request.auth.LoginRequest;
import com.khahnm04.ecommerce.dto.request.auth.LogoutRequest;
import com.khahnm04.ecommerce.dto.request.auth.RefreshTokenRequest;
import com.khahnm04.ecommerce.dto.response.auth.AdminLoginResponse;
import com.khahnm04.ecommerce.dto.response.auth.RefreshTokenResponse;
import com.khahnm04.ecommerce.dto.response.auth.TokenResponse;
import com.khahnm04.ecommerce.entity.user.AccountAdmin;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.repository.user.AccountAdminRepository;
import com.khahnm04.ecommerce.service.auth.AdminAuthService;
import com.khahnm04.ecommerce.service.jwt.JwtService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;
import java.time.Instant;

@Slf4j(topic = "ADMIN-AUTH-SERVICE")
@Service
@RequiredArgsConstructor
public class AdminAuthServiceImpl implements AdminAuthService {

    private final JwtService jwtService;
    private final AccountAdminRepository accountAdminRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String WHITELIST_TOKEN = "whitelist_token:";

    @Override
    @Transactional
    public AdminLoginResponse login(LoginRequest request) {
        AccountAdmin accountAdmin = accountAdminRepository.findByEmailAndIsDeletedFalse(request.email())
            .orElseThrow(() -> new AppException(ErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.password(), accountAdmin.getPassword())) {
            throw new AppException(ErrorCode.LOGIN_FAILED);
        }

        accountAdmin.setLastLoginAt(Instant.now());
        accountAdminRepository.save(accountAdmin);

        TokenResponse accessToken = jwtService.generateAccessToken(accountAdmin.getId(), accountAdmin.getFullName());
        TokenResponse refreshToken = jwtService.generateRefreshToken(accountAdmin.getId());
        saveRefreshTokenToRedis(refreshToken);

        log.info("Account admin with email = {} has been authenticated", accountAdmin.getEmail());
        return AdminLoginResponse.builder()
            .userId(accountAdmin.getId())
            .accessToken(accessToken.token())
            .refreshToken(refreshToken.token())
            .build();
    }

    @Override
    @Transactional
    public void logout(LogoutRequest request) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(request.refreshToken());
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            redisTemplate.delete(WHITELIST_TOKEN + jwtId);
            log.info("Remove refresh_token from whitelist and logout successfully");
        } catch (ParseException e) {
            log.error("Cannot parse token and logout failed");
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    @Transactional
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        try {
            SignedJWT signedJWT = jwtService.verifyToken(request.refreshToken());
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();

            if (!redisTemplate.hasKey(WHITELIST_TOKEN + jwtId)) {
                log.warn("===== KEY REDIS : {}", WHITELIST_TOKEN + jwtId);
                throw new AppException(ErrorCode.REFRESH_TOKEN_FAILED);
            }

            Long userId = Long.valueOf(signedJWT.getJWTClaimsSet().getSubject());

            AccountAdmin accountAdmin = accountAdminRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_ADMIN_NOT_FOUND));

            TokenResponse newAccessToken = jwtService.generateAccessToken(accountAdmin.getId(), accountAdmin.getFullName());
            TokenResponse newRefreshToken = jwtService.generateRefreshToken(accountAdmin.getId());

            redisTemplate.delete(WHITELIST_TOKEN + jwtId);
            saveRefreshTokenToRedis(newRefreshToken);

            log.info("Refresh token successfully, userId: {}", userId);
            return RefreshTokenResponse.builder()
                .accessToken(newAccessToken.token())
                .refreshToken(newRefreshToken.token())
                .build();
        } catch (ParseException e) {
            throw new AppException(ErrorCode.REFRESH_TOKEN_FAILED);
        }
    }

    private void saveRefreshTokenToRedis(TokenResponse refreshToken) {
        long ttlSeconds = refreshToken
            .expiredTime()
            .getTime() / 1000 - Instant.now().getEpochSecond();

        redisTemplate.opsForValue().set(
            WHITELIST_TOKEN + refreshToken.jwtId(),
            refreshToken.jwtId(),
            ttlSeconds,
            TimeUnit.SECONDS
        );
    }

}
