package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.request.ChangePasswordRequest;
import com.khahnm04.ecommerce.dto.request.LoginRequest;
import com.khahnm04.ecommerce.dto.request.ResetPasswordRequest;
import com.khahnm04.ecommerce.dto.request.RegisterRequest;
import com.khahnm04.ecommerce.dto.request.SendOtpRequest;
import com.khahnm04.ecommerce.dto.request.VerifyOtpRequest;
import com.khahnm04.ecommerce.dto.response.IntrospectResponse;
import com.khahnm04.ecommerce.dto.response.LoginResponse;
import com.khahnm04.ecommerce.dto.response.RegisterResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    LoginResponse loginEmployee(LoginRequest request, HttpServletResponse response);

    LoginResponse loginCustomer(LoginRequest request, HttpServletResponse response);

    RegisterResponse register(RegisterRequest request);

    void logout(String token, HttpServletResponse response);

    void changePasswordEmployee(Long employeeId, ChangePasswordRequest request);

    void changePasswordCustomer(Long customerId, ChangePasswordRequest request);

    void resetPasswordEmployee(Long employeeId, ResetPasswordRequest request);

    void resetPasswordCustomer(Long customerId, ResetPasswordRequest request);

    IntrospectResponse introspect(String token);

    void sendOtp(SendOtpRequest request);

    void verifyOtp(VerifyOtpRequest request);

}
