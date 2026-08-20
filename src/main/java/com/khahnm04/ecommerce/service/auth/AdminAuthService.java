package com.khahnm04.ecommerce.service.auth;

import com.khahnm04.ecommerce.dto.request.auth.LoginRequest;
import com.khahnm04.ecommerce.dto.request.auth.LogoutRequest;
import com.khahnm04.ecommerce.dto.request.auth.RefreshTokenRequest;
import com.khahnm04.ecommerce.dto.response.auth.AdminLoginResponse;
import com.khahnm04.ecommerce.dto.response.auth.RefreshTokenResponse;

public interface AdminAuthService {

    AdminLoginResponse login(LoginRequest request);

    void logout(LogoutRequest request);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

}
