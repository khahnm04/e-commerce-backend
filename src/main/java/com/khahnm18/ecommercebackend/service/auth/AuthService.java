package com.khahnm18.ecommercebackend.service.auth;

import com.khahnm18.ecommercebackend.dto.request.auth.*;
import com.khahnm18.ecommercebackend.dto.response.auth.LoginResponse;
import com.khahnm18.ecommercebackend.dto.response.auth.RegisterResponse;
import com.khahnm18.ecommercebackend.dto.response.auth.RefreshTokenResponse;

public interface AuthService {

    LoginResponse loginAdmin(LoginRequest request);

    LoginResponse loginClient(LoginRequest request);

    RegisterResponse registerClient(RegisterRequest request);

    void logout(LogoutRequest request);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

}
