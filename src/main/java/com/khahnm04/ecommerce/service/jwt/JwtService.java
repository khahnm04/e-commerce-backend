package com.khahnm04.ecommerce.service.jwt;

import com.khahnm04.ecommerce.dto.response.auth.TokenResponse;
import com.nimbusds.jwt.SignedJWT;

public interface JwtService {

    TokenResponse generateAccessToken(Long userId, String fullName);

    TokenResponse generateRefreshToken(Long userId);

    SignedJWT verifyToken(String token);

}
