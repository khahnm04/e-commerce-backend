package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.response.TokenPayload;
import com.khahnm04.ecommerce.entity.User;
import com.nimbusds.jwt.SignedJWT;

public interface JwtService {

    SignedJWT validateToken(String token);

    TokenPayload generateToken(User user, boolean rememberMe);

}
