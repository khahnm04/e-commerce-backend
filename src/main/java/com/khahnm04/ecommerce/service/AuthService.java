package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.request.ChangePasswordRequest;
import com.khahnm04.ecommerce.dto.request.LoginRequest;
import com.khahnm04.ecommerce.dto.response.IntrospectResponse;
import com.khahnm04.ecommerce.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request, HttpServletResponse response);

    void logout(String token, HttpServletResponse response);

    void changePassword(Long userId, ChangePasswordRequest request);

    IntrospectResponse introspect(String token);

}
