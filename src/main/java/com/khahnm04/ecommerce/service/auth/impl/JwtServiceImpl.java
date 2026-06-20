package com.khahnm04.ecommerce.service.auth.impl;

import com.khahnm04.ecommerce.dto.request.auth.TokenRequest;
import com.khahnm04.ecommerce.dto.response.auth.TokenResponse;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.service.auth.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jose.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.experimental.NonFinal;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import static com.khahnm04.ecommerce.constant.TokenConstant.ACCESS_TOKEN;
import static com.khahnm04.ecommerce.constant.TokenConstant.REFRESH_TOKEN;

@Slf4j(topic = "JWT-SERVICE")
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final RedisTemplate<String, Object> redisTemplate;

    @NonFinal
    @Value("${jwt.secret-key}")
    private String signerKey;

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
    public TokenResponse generateAccessToken(TokenRequest request) {
        return generateToken(request, ACCESS_TOKEN);
    }

    @Override
    public TokenResponse generateRefreshToken(TokenRequest request) {
        return generateToken(request, REFRESH_TOKEN);
    }

    @Override
    public SignedJWT verifyToken(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
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

    private TokenResponse generateToken(TokenRequest request, String typeToken) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        long expiration = getExpiration(typeToken, request.rememberMe());
        Date issueTime = new Date();
        Date expiredTime = new Date(Instant.now()
            .plus(expiration, ChronoUnit.SECONDS)
            .toEpochMilli());
        String jwtId = UUID.randomUUID().toString();

        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
            .subject(request.subject())
            .issueTime(issueTime)
            .expirationTime(expiredTime)
            .jwtID(jwtId);

        if (typeToken.equalsIgnoreCase(ACCESS_TOKEN)) {
            claimsBuilder.claim("userId", request.userId());
            claimsBuilder.claim("scope", request.scope());
        }

        JWTClaimsSet jwtClaimsSet = claimsBuilder.build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new AppException(ErrorCode.TOKEN_CREATION_FAILED);
        }

        String token = jwsObject.serialize();
        return TokenResponse.builder()
            .jwtId(jwtId)
            .token(token)
            .expiredTime(expiredTime)
            .build();
    }

    private long getExpiration(String typeToken, boolean rememberMe) {
        if (typeToken.equalsIgnoreCase(ACCESS_TOKEN)) {
            return accessTokenExpiration;
        }
        return rememberMe ? rememberMeExpiration : refreshTokenExpiration;
    }

}
