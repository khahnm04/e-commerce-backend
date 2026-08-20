package com.khahnm04.ecommerce.service.jwt;

import com.khahnm04.ecommerce.dto.response.auth.TokenResponse;
import com.khahnm04.ecommerce.entity.auth.Role;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.properties.JwtProperties;
import com.khahnm04.ecommerce.repository.auth.AccountAdminRoleRepository;
import org.springframework.stereotype.Service;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jose.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j(topic = "JWT-SERVICE")
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;
    private final AccountAdminRoleRepository accountAdminRoleRepository;

    @Override
    public TokenResponse generateAccessToken(Long userId, String fullName) {
        JWSAlgorithm algorithm = JWSAlgorithm.HS512;
        JWSHeader header = new JWSHeader(algorithm);

        Date issueTime = new Date();
        Date expiredTime = new Date(Instant.now()
            .plus(jwtProperties.getAccessTokenExpiration(), ChronoUnit.SECONDS)
            .toEpochMilli());
        String jwtId = UUID.randomUUID().toString();

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
            .subject(String.valueOf(userId))
            .issueTime(issueTime)
            .expirationTime(expiredTime)
            .jwtID(jwtId)
            .claim("scope", buildScope(userId))
            .claim("fullName", fullName)
            .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(jwtProperties.getSecretKey().getBytes()));
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.TOKEN_GENERATION_FAILED);
        }
        String token = jwsObject.serialize();
        return TokenResponse.builder()
            .jwtId(jwtId)
            .token(token)
            .expiredTime(expiredTime)
            .build();
    }

    @Override
    public TokenResponse generateRefreshToken(Long userId) {
        JWSAlgorithm algorithm = JWSAlgorithm.HS512;
        JWSHeader header = new JWSHeader(algorithm);

        Date issueTime = new Date();
        Date expiredTime = new Date(Instant.now()
            .plus(jwtProperties.getRefreshTokenExpiration(), ChronoUnit.SECONDS)
            .toEpochMilli());
        String jwtId = UUID.randomUUID().toString();

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
            .subject(String.valueOf(userId))
            .issueTime(issueTime)
            .expirationTime(expiredTime)
            .jwtID(jwtId)
            .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(jwtProperties.getSecretKey().getBytes()));
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.TOKEN_GENERATION_FAILED);
        }
        String token = jwsObject.serialize();
        return TokenResponse.builder()
            .jwtId(jwtId)
            .token(token)
            .expiredTime(expiredTime)
            .build();
    }

    @Override
    public SignedJWT verifyToken(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(jwtProperties.getSecretKey().getBytes());
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

    private String buildScope(Long userid) {
        List<Role> roles = accountAdminRoleRepository.findRolesByAccountAdminId(userid);
        if (CollectionUtils.isEmpty(roles)) {
            return "";
        }
        StringJoiner scope = new StringJoiner(" ");
        roles.forEach(role -> scope.add("ROLE_" + role.getCode()));
        return scope.toString();
    }

}
