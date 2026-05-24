package com.khahnm04.ecommerce.service.auth;

import com.khahnm04.ecommerce.dto.request.auth.AdminLoginRequest;
import com.khahnm04.ecommerce.dto.request.auth.LogoutRequest;
import com.khahnm04.ecommerce.dto.request.auth.RefreshTokenRequest;
import com.khahnm04.ecommerce.dto.response.auth.AdminLoginResponse;
import com.khahnm04.ecommerce.dto.response.auth.IntrospectResponse;
import com.khahnm04.ecommerce.dto.response.auth.RefreshTokenResponse;

public interface AdminAuthService {

    AdminLoginResponse login(AdminLoginRequest request);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

    void logout(LogoutRequest request);

    IntrospectResponse introspect(String accessToken);

}
