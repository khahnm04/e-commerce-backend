package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.response.TokenPayload;
import com.khahnm04.ecommerce.entity.User;
import com.nimbusds.jwt.SignedJWT;

public interface JwtService {

    TokenPayload generateAccessToken(User user);

    TokenPayload generateRefreshToken(User user, boolean rememberMe);

    SignedJWT verifyAccessToken(String token);

    SignedJWT verifyRefreshToken(String token);

}
