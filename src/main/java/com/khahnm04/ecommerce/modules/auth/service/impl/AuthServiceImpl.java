package com.khahnm04.ecommerce.modules.auth.service.impl;

import com.khahnm04.ecommerce.modules.auth.entity.User;
import com.khahnm04.ecommerce.modules.auth.repository.UserRepository;
import com.khahnm04.ecommerce.common.constant.TokenConstants;
import com.khahnm04.ecommerce.common.security.CookieProperties;
import com.khahnm04.ecommerce.modules.auth.dto.request.ChangePasswordRequest;
import com.khahnm04.ecommerce.modules.auth.dto.request.LoginRequest;
import com.khahnm04.ecommerce.modules.auth.dto.response.LoginResponse;
import com.khahnm04.ecommerce.modules.auth.dto.response.IntrospectResponse;
import com.khahnm04.ecommerce.modules.auth.dto.response.TokenPayload;
import com.khahnm04.ecommerce.modules.auth.entity.RedisToken;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.modules.auth.repository.RedisTokenRepository;
import com.khahnm04.ecommerce.modules.auth.service.AuthService;
import com.khahnm04.ecommerce.modules.auth.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.nimbusds.jwt.SignedJWT;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RedisTokenRepository redisTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CookieProperties cookieProperties;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request, HttpServletResponse response) {
        String identifier = null;
        if (StringUtils.hasText(request.email())) {
            identifier = request.email();
        } else if (StringUtils.hasText(request.phoneNumber())) {
            identifier = request.phoneNumber();
        } else {
            throw new AppException(ErrorCode.LOGIN_IDENTIFIER_REQUIRED);
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(identifier, request.password()));

        User user = (User) authentication.getPrincipal();

        TokenPayload accessToken = jwtService.generateAccessToken(user);
        TokenPayload refreshToken = jwtService.generateRefreshToken(user);

        redisTokenRepository.save(RedisToken.builder()
                .jwtId(refreshToken.jwtId())
                .expiredTime(refreshToken.expiredTime().getTime())
                .build());

        addAccessTokenCookies(response, accessToken.token());
        addRefreshTokenCookies(response, refreshToken.token());

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("User with id = {} has been authenticated", user.getId());
        return LoginResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .avatar(user.getAvatar())
                .build();
    }

    @Override
    public void logout(String accessToken, String refreshToken, HttpServletResponse response) {
        try {
            // save access token
            var signAccessToken = jwtService.verifyAccessToken(accessToken);
            redisTokenRepository.save(RedisToken.builder()
                    .jwtId(signAccessToken.getJWTClaimsSet().getJWTID())
                    .expiredTime(signAccessToken.getJWTClaimsSet().getExpirationTime().getTime())
                    .build());
            log.info("save access token success");

            // remove refresh token
            var signRefreshToken = jwtService.verifyRefreshToken(refreshToken);
            redisTokenRepository.deleteById(signRefreshToken.getJWTClaimsSet().getJWTID());
            log.info("remove refresh token success");

            // delete token from cookie
            clearAccessTokenCookie(response);
            clearRefreshTokenCookie(response);

        } catch (AppException | ParseException e) {
            log.error("Token already expired");
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    @Override
    public void refreshToken(String token, HttpServletResponse response) {
        try {
            SignedJWT signedJWT = jwtService.verifyRefreshToken(token);

            User user = userRepository.findByIdentifier(signedJWT.getJWTClaimsSet().getSubject())
                    .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_FAILED));

            TokenPayload accessToken = jwtService.generateAccessToken(user);
            addAccessTokenCookies(response, accessToken.token());

            log.info("refresh token success");
        } catch (ParseException e) {
            log.error("cannot refresh token");
            throw new AppException(ErrorCode.REFRESH_TOKEN_FAILED);
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

    // ======================== Private methods ========================

    private void addAccessTokenCookies(HttpServletResponse response, String accessToken) {
        ResponseCookie accessCookie = ResponseCookie.from(TokenConstants.ACCESS_TOKEN, accessToken)
                .httpOnly(cookieProperties.isHttpOnly())
                .secure(cookieProperties.isSecure())
                .domain(cookieProperties.getDomain())
                .path("/")
                .maxAge(cookieProperties.getAccessTokenMaxAge())
                .sameSite(cookieProperties.getSameSite())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
    }

    private void addRefreshTokenCookies(HttpServletResponse response, String refreshToken) {
        ResponseCookie refreshCookie = ResponseCookie.from(TokenConstants.REFRESH_TOKEN, refreshToken)
                .httpOnly(cookieProperties.isHttpOnly())
                .secure(cookieProperties.isSecure())
                .domain(cookieProperties.getDomain())
                .path("/")
                .maxAge(cookieProperties.getRefreshTokenMaxAge())
                .sameSite(cookieProperties.getSameSite())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    public void clearAccessTokenCookie(HttpServletResponse response) {
        ResponseCookie accessTokenCookie = ResponseCookie.from(TokenConstants.ACCESS_TOKEN, "")
                .maxAge(0)
                .domain(cookieProperties.getDomain())
                .path("/")
                .httpOnly(cookieProperties.isHttpOnly())
                .secure(cookieProperties.isSecure())
                .sameSite(cookieProperties.getSameSite())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
    }

    public void clearRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie refreshTokenCookie = ResponseCookie.from(TokenConstants.REFRESH_TOKEN, "")
                .maxAge(0)
                .domain(cookieProperties.getDomain())
                .path("/")
                .httpOnly(cookieProperties.isHttpOnly())
                .secure(cookieProperties.isSecure())
                .sameSite(cookieProperties.getSameSite())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

}
