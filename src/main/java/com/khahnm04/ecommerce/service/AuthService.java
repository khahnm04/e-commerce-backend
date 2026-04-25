package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.request.ChangePasswordRequest;
import com.khahnm04.ecommerce.dto.request.LoginRequest;
import com.khahnm04.ecommerce.dto.request.ResetPasswordRequest;
import com.khahnm04.ecommerce.dto.request.RegisterRequest;
import com.khahnm04.ecommerce.dto.response.IntrospectResponse;
import com.khahnm04.ecommerce.dto.response.LoginResponse;
import com.khahnm04.ecommerce.dto.response.RegisterResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request, HttpServletResponse response);

    RegisterResponse register(RegisterRequest request);

    void logout(String token, HttpServletResponse response);

    void changePassword(Long userId, ChangePasswordRequest request);

    void resetPassword(Long userId, ResetPasswordRequest request);

    IntrospectResponse introspect(String token);

}
