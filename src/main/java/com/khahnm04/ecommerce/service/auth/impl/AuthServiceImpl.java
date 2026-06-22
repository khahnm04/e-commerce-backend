package com.khahnm04.ecommerce.service.auth.impl;

import com.khahnm04.ecommerce.dto.request.auth.LoginRequest;
import com.khahnm04.ecommerce.dto.response.auth.LoginResponse;
import com.khahnm04.ecommerce.dto.request.auth.TokenRequest;
import com.khahnm04.ecommerce.dto.response.auth.TokenResponse;
import com.khahnm04.ecommerce.dto.response.user.AccountUserResponse;
import com.khahnm04.ecommerce.dto.response.user.AccountAdminResponse;
import com.khahnm04.ecommerce.entity.auth.Role;
import com.khahnm04.ecommerce.constant.auth.UserType;
import com.khahnm04.ecommerce.entity.user.AccountUser;
import com.khahnm04.ecommerce.entity.user.AccountAdmin;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.repository.auth.AccountAdminRoleRepository;
import com.khahnm04.ecommerce.repository.user.AccountUserRepository;
import com.khahnm04.ecommerce.repository.user.AccountAdminRepository;
import com.khahnm04.ecommerce.service.auth.AuthService;
import com.khahnm04.ecommerce.service.auth.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.stereotype.Service;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.StringJoiner;
import java.time.Instant;
import java.util.List;
import static com.khahnm04.ecommerce.constant.RedisConstant.BLACKLIST_TOKEN;
import static com.khahnm04.ecommerce.constant.RedisConstant.WHITELIST_TOKEN;
import static com.khahnm04.ecommerce.constant.TokenConstant.ACCESS_TOKEN;
import static com.khahnm04.ecommerce.constant.TokenConstant.REFRESH_TOKEN;

@Slf4j(topic = "AUTH-SERVICE")
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final AccountAdminRepository accountAdminRepository;
    private final AccountAdminRoleRepository accountAdminRoleRepository;
    private final AccountUserRepository accountUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;

    @NonFinal
    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @NonFinal
    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    @NonFinal
    @Value("${jwt.remember-me-expiration}")
    private Long rememberMeExpiration;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request, HttpServletResponse response) {
        Pair<TokenRequest, LoginResponse> result = request.userType() == UserType.ACCOUNT_ADMIN
            ? loginAsAccountAdmin(request)
            : loginAsAccountUser(request);

        TokenResponse accessToken = jwtService.generateAccessToken(result.getFirst());
        TokenResponse refreshToken = jwtService.generateRefreshToken(result.getFirst());

        saveRefreshTokenToRedis(refreshToken);
        addAccessTokenToCookie(response, accessToken.token());
        addRefreshTokenToCookie(response, refreshToken.token(), request.rememberMe());

        return result.getSecond();
    }

    @Override
    @Transactional
    public void logout(String accessToken, String refreshToken, HttpServletResponse response) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(refreshToken);
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            redisTemplate.delete(WHITELIST_TOKEN + jwtId);
            log.info("Remove refresh_token from whitelist successfully");

            SignedJWT accessJWT = SignedJWT.parse(accessToken);
            String accessJwtId = accessJWT.getJWTClaimsSet().getJWTID();
            Date accessExpiredTime = accessJWT.getJWTClaimsSet().getExpirationTime();
            long ttlSeconds = accessExpiredTime.getTime() / 1000 - Instant.now().getEpochSecond();
            if (ttlSeconds > 0) {
                redisTemplate.opsForValue().set(
                    BLACKLIST_TOKEN + accessJwtId,
                    accessJwtId,
                    ttlSeconds,
                    TimeUnit.SECONDS
                );
                log.info("Add access_token to blacklist successfully");
            }
            log.info("Logout successfully");
        } catch (ParseException e) {
            log.error("Cannot parse token and logout failed");
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        } finally {
            clearTokenToCookie(response, ACCESS_TOKEN);
            clearTokenToCookie(response, REFRESH_TOKEN);
        }
    }

    @Override
    public void refreshToken(String refreshToken, UserType userType, HttpServletResponse response) {
        try {
            SignedJWT signedJWT = jwtService.verifyToken(refreshToken);
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();

            if (!redisTemplate.hasKey(WHITELIST_TOKEN + jwtId)) {
                throw new AppException(ErrorCode.REFRESH_TOKEN_FAILED);
            }

            String subject = signedJWT.getJWTClaimsSet().getSubject();
            TokenRequest tokenRequest;

            if (userType == UserType.ACCOUNT_ADMIN) {
                 AccountAdmin accountAdmin = accountAdminRepository
                     .findByEmailAndIsDeletedFalse(subject)
                    .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_ADMIN_NOT_FOUND));

                tokenRequest = TokenRequest.builder()
                    .userId(accountAdmin.getId())
                    .subject(accountAdmin.getEmail())
                    .scope(buildScope(accountAdmin))
                    .userType(userType)
                    .build();
            } else {
                AccountUser accountUser = accountUserRepository
                    .findByEmailAndIsDeletedFalse(subject)
                    .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_USER_NOT_FOUND));

                tokenRequest = TokenRequest.builder()
                    .userId(accountUser.getId())
                    .subject(accountUser.getEmail())
                    .scope("ROLE_CUSTOMER")
                    .userType(userType)
                    .build();
            }

            TokenResponse newAccessToken = jwtService.generateAccessToken(tokenRequest);
            TokenResponse newRefreshToken = jwtService.generateRefreshToken(tokenRequest);

            addAccessTokenToCookie(response, newAccessToken.token());
            addRefreshTokenToCookie(response, newRefreshToken.token(), true);

            redisTemplate.delete(WHITELIST_TOKEN + jwtId);
            saveRefreshTokenToRedis(newRefreshToken);

            log.info("Refresh token successfully: {}", subject);
        } catch (ParseException e) {
            log.error("Cannot parse refresh token");
            throw new AppException(ErrorCode.REFRESH_TOKEN_FAILED);
        }
    }

    @Override
    public void clearCookie(HttpServletResponse response) {
        clearTokenToCookie(response, ACCESS_TOKEN);
        clearTokenToCookie(response, REFRESH_TOKEN);
    }

    private Pair<TokenRequest, LoginResponse> loginAsAccountAdmin(LoginRequest request) {
        AccountAdmin accountAdmin = accountAdminRepository.findByEmailAndIsDeletedFalse(request.email())
            .orElseThrow(() -> new AppException(ErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.password(), accountAdmin.getPassword())) {
            throw new AppException(ErrorCode.LOGIN_FAILED);
        }

        accountAdmin.setLastLoginAt(Instant.now());
        accountAdminRepository.save(accountAdmin);
        log.info("Account admin with email = {} has been authenticated", accountAdmin.getEmail());

        TokenRequest tokenRequest = TokenRequest.builder()
            .userId(accountAdmin.getId())
            .subject(accountAdmin.getEmail())
            .scope(buildScope(accountAdmin))
            .userType(UserType.ACCOUNT_ADMIN)
            .rememberMe(request.rememberMe())
            .build();

        LoginResponse loginResponse = LoginResponse.builder()
            .accountAdmin(AccountAdminResponse.builder()
                .id(accountAdmin.getId())
                .jobTitle(accountAdmin.getJobTitle())
                .email(accountAdmin.getEmail())
                .fullName(accountAdmin.getFullName())
                .avatar(accountAdmin.getAvatar())
                .build())
            .userType(UserType.ACCOUNT_ADMIN)
            .build();

        return Pair.of(tokenRequest, loginResponse);
    }

    private Pair<TokenRequest, LoginResponse> loginAsAccountUser(LoginRequest request) {
        AccountUser accountUser = accountUserRepository.findByEmailAndIsDeletedFalse(request.email())
            .orElseThrow(() -> new AppException(ErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.password(), accountUser.getPassword())) {
            throw new AppException(ErrorCode.LOGIN_FAILED);
        }

        accountUser.setLastLoginAt(Instant.now());
        accountUserRepository.save(accountUser);
        log.info("Account user with email = {} has been authenticated", accountUser.getEmail());

        TokenRequest tokenRequest = TokenRequest.builder()
            .userId(accountUser.getId())
            .subject(accountUser.getEmail())
            .scope("ROLE_ACCOUNT_USER")
            .userType(UserType.ACCOUNT_USER)
            .rememberMe(request.rememberMe())
            .build();

        LoginResponse loginResponse = LoginResponse.builder()
            .accountUser(AccountUserResponse.builder()
                .id(accountUser.getId())
                .email(accountUser.getEmail())
                .fullName(accountUser.getFullName())
                .avatar(accountUser.getAvatar())
                .build())
            .userType(UserType.ACCOUNT_USER)
            .build();

        return Pair.of(tokenRequest, loginResponse);
    }

    private String buildScope(AccountAdmin accountAdmin) {
        List<Role> roles = accountAdminRoleRepository.findRolesByAccountAdminId(accountAdmin.getId());
        if (CollectionUtils.isEmpty(roles)) {
            return "";
        }
        StringJoiner scope = new StringJoiner(" ");
        roles.forEach(role -> scope.add("ROLE_" + role.getCode()));
        return scope.toString();
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

    private void addAccessTokenToCookie(HttpServletResponse response, String accessToken) {
        ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN, accessToken)
            .httpOnly(true)
            .secure(false)
            .domain("localhost")
            .path("/")
            .maxAge(accessTokenExpiration)
            .sameSite("Lax")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken, Boolean rememberMe) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, refreshToken)
            .httpOnly(true)
            .secure(false)
            .domain("localhost")
            .path("/")
            .maxAge(rememberMe ? rememberMeExpiration : refreshTokenExpiration)
            .sameSite("Lax")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void clearTokenToCookie(HttpServletResponse response, String tokenType) {
        ResponseCookie cookie = ResponseCookie.from(tokenType, "")
            .maxAge(0)
            .domain("localhost")
            .path("/")
            .httpOnly(true)
            .secure(false)
            .sameSite("Lax")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

}
