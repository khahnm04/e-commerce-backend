package com.khahnm18.ecommercebackend.service.auth;

import com.khahnm18.ecommercebackend.dto.request.auth.LoginRequest;
import com.khahnm18.ecommercebackend.dto.response.auth.LoginResponse;
import com.khahnm18.ecommercebackend.dto.request.auth.LogoutRequest;
import com.khahnm18.ecommercebackend.dto.request.auth.RefreshTokenRequest;
import com.khahnm18.ecommercebackend.dto.response.auth.RefreshTokenResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    void logout(LogoutRequest request);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

}
