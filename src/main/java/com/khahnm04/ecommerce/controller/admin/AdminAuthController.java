package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.dto.request.ChangePasswordRequest;
import com.khahnm04.ecommerce.dto.request.LoginRequest;
import com.khahnm04.ecommerce.dto.request.ResetPasswordRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.IntrospectResponse;
import com.khahnm04.ecommerce.dto.response.LoginResponse;
import com.khahnm04.ecommerce.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        return ApiResponse.<LoginResponse>builder()
                .data(authService.loginEmployee(request, response))
                .message("Login successfully")
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
        Long employeeId = jwt.getClaim("userId");
        authService.changePasswordEmployee(employeeId, request);
        return ApiResponse.<Void>builder()
                .message("Password changed successfully")
                .build();
    }

    @PutMapping("/reset-password/{id}")
    public ApiResponse<Void> resetPassword(
            @PathVariable Long id,
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        authService.resetPasswordEmployee(id, request);
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

}
