package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.entity.User;
import com.khahnm04.ecommerce.common.constant.TokenConstants;
import com.khahnm04.ecommerce.dto.response.TokenPayload;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.repository.RedisTokenRepository;
import com.khahnm04.ecommerce.security.JwtProperties;
import com.khahnm04.ecommerce.service.JwtService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final RedisTokenRepository redisTokenRepository;
    private final JwtProperties jwtProperties;

    @Override
    public TokenPayload generateAccessToken(User user) {
        return generateToken(user, TokenConstants.ACCESS_TOKEN, false);
    }

    @Override
    public TokenPayload generateRefreshToken(User user, boolean rememberMe) {
        return generateToken(user, TokenConstants.REFRESH_TOKEN, rememberMe);
    }

    @Override
    public SignedJWT verifyAccessToken(String token) {
        SignedJWT signedJWT = verifyToken(token);
        if (isExistTokenInRedis(signedJWT)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }

    @Override
    public SignedJWT verifyRefreshToken(String token) {
        SignedJWT signedJWT = verifyToken(token);
        if (!isExistTokenInRedis(signedJWT)) {
            throw new AppException(ErrorCode.REFRESH_TOKEN_FAILED);
        }
        return signedJWT;
    }

    private TokenPayload generateToken(User user, String typeToken, boolean rememberMe) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        String subject = StringUtils.hasText(user.getPhoneNumber()) ? user.getPhoneNumber() : user.getEmail();
        Date issueTime = new Date();
        Date expiredTime = new Date(Instant.now()
                .plus(typeToken.equalsIgnoreCase(TokenConstants.ACCESS_TOKEN)
                        ? jwtProperties.getAccessTokenExpiration()
                        : rememberMe
                              ? jwtProperties.getRefreshTokenRememberExpiration()
                              : jwtProperties.getRefreshTokenExpiration(), ChronoUnit.SECONDS)
                .toEpochMilli());
        String jwtId = UUID.randomUUID().toString();

        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                .subject(subject)
                .issueTime(issueTime)
                .expirationTime(expiredTime)
                .jwtID(jwtId)
                .claim("scope", buildScope(user))
                .claim("type", typeToken);

        if (typeToken.equalsIgnoreCase(TokenConstants.ACCESS_TOKEN)) {
            claimsBuilder.claim("userId", user.getId());
        }

        JWTClaimsSet jwtClaimsSet = claimsBuilder.build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(jwtProperties.getSignerKey().getBytes()));
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new AppException(ErrorCode.TOKEN_CREATION_FAILED);
        }

        String token = jwsObject.serialize();
        return TokenPayload.builder()
                .jwtId(jwtId)
                .token(token)
                .expiredTime(expiredTime)
                .build();
    }

    private SignedJWT verifyToken(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(jwtProperties.getSignerKey().getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);

            var verified = signedJWT.verify(verifier);
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            if (!verified || !expiryTime.after(new Date())) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }

            return signedJWT;
        } catch (ParseException | JOSEException e) {
            log.error("Cannot verify token", e);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    private String buildScope(User user) {
        StringJoiner scope = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                scope.add("ROLE_" + role.getCode());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions()
                            .forEach(permission -> scope.add(permission.getCode()));
                }
            });
        }
        return scope.toString();
    }

    private boolean isExistTokenInRedis(SignedJWT signedJWT) {
        try {
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            return redisTokenRepository.existsById(jwtId);
        } catch (ParseException e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

}
