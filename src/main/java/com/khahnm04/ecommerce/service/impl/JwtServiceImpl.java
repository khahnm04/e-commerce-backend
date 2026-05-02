package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.common.enums.UserType;
import com.khahnm04.ecommerce.dto.response.TokenPayload;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.repository.auth.RedisTokenRepository;
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
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final RedisTokenRepository redisTokenRepository;
    private final JwtProperties jwtProperties;

    @Override
    public SignedJWT validateToken(String token) {
        SignedJWT signedJWT = verifyToken(token);
        if (isExistTokenInRedis(signedJWT)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }

    @Override
    public TokenPayload generateToken(Long userId, String subject, String scope, UserType userType, boolean rememberMe) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        Date issueTime = new Date();
        Date expiredTime = new Date(Instant.now()
                .plus(rememberMe
                      ? jwtProperties.getTokenRememberExpiration()
                      : jwtProperties.getTokenExpiration(), ChronoUnit.SECONDS)
                .toEpochMilli());
        String jwtId = UUID.randomUUID().toString();

        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                .subject(subject)
                .issueTime(issueTime)
                .expirationTime(expiredTime)
                .jwtID(jwtId)
                .claim("userId", userId)
                .claim("userType", userType.name())
                .claim("scope", scope);

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

    private boolean isExistTokenInRedis(SignedJWT signedJWT) {
        try {
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            return redisTokenRepository.existsById(jwtId);
        } catch (ParseException e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

}
