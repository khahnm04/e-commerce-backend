package com.khahnm04.ecommerce.service.auth;

import com.khahnm04.ecommerce.constant.auth.UserType;
import com.khahnm04.ecommerce.dto.request.auth.LoginRequest;
import com.khahnm04.ecommerce.dto.response.auth.LoginResponse;
import com.khahnm04.ecommerce.dto.response.auth.RefreshTokenResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request, HttpServletResponse response);

    void logout(String accessToken, String refreshToken, HttpServletResponse response);

    RefreshTokenResponse refreshToken(String refreshToken, UserType userType, HttpServletResponse response);

}
