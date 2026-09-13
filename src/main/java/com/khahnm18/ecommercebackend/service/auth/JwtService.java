package com.khahnm18.ecommercebackend.service.auth;

import com.khahnm18.ecommercebackend.dto.response.auth.TokenResponse;
import com.khahnm18.ecommercebackend.entity.auth.Account;
import com.nimbusds.jwt.SignedJWT;

public interface JwtService {

    TokenResponse generateAccessToken(Account account);

    TokenResponse generateRefreshToken(Account account);

    SignedJWT verifyToken(String token);

}
