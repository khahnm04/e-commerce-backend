package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.common.enums.UserType;
import com.khahnm04.ecommerce.dto.response.TokenPayload;
import com.nimbusds.jwt.SignedJWT;

public interface JwtService {

    SignedJWT validateToken(String token);

    TokenPayload generateToken(Long userId, String subject, String scope, UserType userType, boolean rememberMe);

}
