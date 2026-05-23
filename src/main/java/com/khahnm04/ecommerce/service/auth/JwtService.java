package com.khahnm04.ecommerce.service.auth;

import com.khahnm04.ecommerce.dto.request.auth.TokenRequest;
import com.khahnm04.ecommerce.dto.response.auth.TokenResponse;
import com.nimbusds.jwt.SignedJWT;

public interface JwtService {

    TokenResponse generateAccessToken(TokenRequest request);

    TokenResponse generateRefreshToken(TokenRequest request);

    SignedJWT verifyAccessToken(String token);

    SignedJWT verifyRefreshToken(String token);

}
