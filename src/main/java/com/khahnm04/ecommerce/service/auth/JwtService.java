package com.khahnm04.ecommerce.service.auth;

import com.khahnm04.ecommerce.dto.response.auth.TokenPayload;
import com.khahnm04.ecommerce.entity.auth.User;
import com.nimbusds.jwt.SignedJWT;

public interface JwtService {

    TokenPayload generateAccessToken(User user);

    TokenPayload generateRefreshToken(User user, boolean rememberMe);

    SignedJWT verifyAccessToken(String token);

    SignedJWT verifyRefreshToken(String token);

}
