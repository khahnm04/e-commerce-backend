package com.khahnm18.ecommercebackend.service.auth.impl;

import com.khahnm18.ecommercebackend.dto.response.auth.TokenResponse;
import com.khahnm18.ecommercebackend.entity.auth.Account;
import com.khahnm18.ecommercebackend.entity.auth.Role;
import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.config.properties.JwtProperties;
import com.khahnm18.ecommercebackend.repository.auth.RoleRepository;
import com.khahnm18.ecommercebackend.service.auth.JwtService;
import org.springframework.util.CollectionUtils;
import org.springframework.stereotype.Service;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.Payload;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.time.Instant;
import java.util.StringJoiner;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j(topic = "JWT-SERVICE")
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;
    private final RoleRepository roleRepository;

    @Override
    public TokenResponse generateAccessToken(Account account) {
        JWSAlgorithm algorithm = JWSAlgorithm.HS512;
        JWSHeader header = new JWSHeader.Builder(algorithm)
            .type(JOSEObjectType.JWT)
            .build();

        Date issueTime = new Date();
        Date expiredTime = new Date(Instant.now()
            .plus(jwtProperties.getAccessTokenExpiration(), ChronoUnit.SECONDS)
            .toEpochMilli());
        String jwtId = UUID.randomUUID().toString();

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
            .subject(String.valueOf(account.getId()))
            .issueTime(issueTime)
            .expirationTime(expiredTime)
            .jwtID(jwtId)
            .claim("scope", buildScope(account.getId()))
            .claim("email", account.getEmail())
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
    public TokenResponse generateRefreshToken(Account account) {
        JWSAlgorithm algorithm = JWSAlgorithm.HS512;
        JWSHeader header = new JWSHeader.Builder(algorithm)
            .type(JOSEObjectType.JWT)
            .build();

        Date issueTime = new Date();
        Date expiredTime = new Date(Instant.now()
            .plus(jwtProperties.getRefreshTokenExpiration(), ChronoUnit.SECONDS)
            .toEpochMilli());
        String jwtId = UUID.randomUUID().toString();

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
            .subject(String.valueOf(account.getId()))
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
        } catch (ParseException | JOSEException ex) {
            log.error("Cannot verify token", ex);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    private String buildScope(UUID accountId) {
        List<Role> roles = roleRepository.findRolesByAccountId(accountId);
        if (CollectionUtils.isEmpty(roles)) {
            return "";
        }
        StringJoiner scope = new StringJoiner(" ");
        roles.forEach(role -> scope.add("ROLE_" + role.getCode()));
        return scope.toString();
    }

}
