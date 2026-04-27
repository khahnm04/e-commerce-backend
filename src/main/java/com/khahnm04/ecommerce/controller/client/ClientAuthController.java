package com.khahnm04.ecommerce.controller.client;

import com.khahnm04.ecommerce.dto.request.ChangePasswordRequest;
import com.khahnm04.ecommerce.dto.request.LoginRequest;
import com.khahnm04.ecommerce.dto.request.RegisterRequest;
import com.khahnm04.ecommerce.dto.request.ResetPasswordRequest;
import com.khahnm04.ecommerce.dto.request.SendOtpRequest;
import com.khahnm04.ecommerce.dto.request.VerifyOtpRequest;
import com.khahnm04.ecommerce.dto.request.ForgotPasswordVerifyOtpRequest;
import com.khahnm04.ecommerce.dto.request.ForgotPasswordResetRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.ResetTokenResponse;
import com.khahnm04.ecommerce.dto.response.IntrospectResponse;
import com.khahnm04.ecommerce.dto.response.LoginResponse;
import com.khahnm04.ecommerce.dto.response.RegisterResponse;
import com.khahnm04.ecommerce.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/client")
@RequiredArgsConstructor
public class ClientAuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        return ApiResponse.<LoginResponse>builder()
                .data(authService.loginCustomer(request, response))
                .message("Login successfully")
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ApiResponse.<RegisterResponse>builder()
                .data(authService.register(request))
                .message("Register successfully")
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(
            @CookieValue(name = "token", required = false) String token,
            HttpServletResponse response
    ) {
        authService.logout(token, response);
        return ApiResponse.<Void>builder()
                .message("Logout successfully")
                .build();
    }

    @PutMapping("/change-password")
    public ApiResponse<Void> changePassword(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        Long customerId = jwt.getClaim("userId");
        authService.changePasswordCustomer(customerId, request);
        return ApiResponse.<Void>builder()
                .message("Password changed successfully")
                .build();
    }

    @PutMapping("/reset-password/{id}")
    public ApiResponse<Void> resetPassword(
            @PathVariable Long id,
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        authService.resetPasswordCustomer(id, request);
        return ApiResponse.<Void>builder()
            .message("Password reset successfully")
            .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(
            @CookieValue(name = "token", required = false) String token
    ) {
        return ApiResponse.<IntrospectResponse>builder()
                .data(authService.introspect(token))
                .message("Introspect successfully")
                .build();
    }

    @PostMapping("/send-otp")
    public ApiResponse<Void> sendOtp(
            @Valid @RequestBody SendOtpRequest request
    ) {
        authService.sendOtp(request);
        return ApiResponse.<Void>builder()
                .message("OTP sent successfully")
                .build();
    }

    @PostMapping("/verify-otp")
    public ApiResponse<Void> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request
    ) {
        authService.verifyOtp(request);
        return ApiResponse.<Void>builder()
                .message("Email verified successfully")
                .build();
    }

    @PostMapping("/forgot-password/verify-otp")
    public ApiResponse<ResetTokenResponse> forgotPasswordVerifyOtp(
            @Valid @RequestBody ForgotPasswordVerifyOtpRequest request
    ) {
        return ApiResponse.<ResetTokenResponse>builder()
                .data(authService.forgotPasswordVerifyOtp(request))
                .message("OTP verified successfully")
                .build();
    }

    @PostMapping("/forgot-password/reset")
    public ApiResponse<Void> forgotPasswordReset(
            @Valid @RequestBody ForgotPasswordResetRequest request
    ) {
        authService.forgotPasswordReset(request);
        return ApiResponse.<Void>builder()
                .message("Password reset successfully")
                .build();
    }

}
