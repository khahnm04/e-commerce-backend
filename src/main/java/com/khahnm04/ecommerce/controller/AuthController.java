package com.khahnm04.ecommerce.controller;

import com.khahnm04.ecommerce.common.constant.TokenConstants;
import com.khahnm04.ecommerce.dto.request.ChangePasswordRequest;
import com.khahnm04.ecommerce.dto.request.LoginRequest;
import com.khahnm04.ecommerce.dto.request.ResetPasswordRequest;
import com.khahnm04.ecommerce.dto.response.IntrospectResponse;
import com.khahnm04.ecommerce.dto.response.LoginResponse;
import com.khahnm04.ecommerce.dto.request.RegisterRequest;
import com.khahnm04.ecommerce.dto.response.RegisterResponse;
import com.khahnm04.ecommerce.service.AuthService;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    ApiResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        return ApiResponse.<LoginResponse>builder()
                .data(authService.login(request, response))
                .message("Login successfully")
                .build();
    }

    @PostMapping("/register")
    ApiResponse<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ApiResponse.<RegisterResponse>builder()
                .data(authService.register(request))
                .message("Register successfully")
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(
            @CookieValue(name = TokenConstants.TOKEN) String token,
            HttpServletResponse response
    ) {
        authService.logout(token, response);
        return ApiResponse.<Void>builder()
                .message("Logout successfully")
                .build();
    }

    @PostMapping("/change-password")
    ApiResponse<Void> changePassword(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        Long userId = jwt.getClaim("userId");
        authService.changePassword(userId, request);
        return ApiResponse.<Void>builder()
                .message("Change password successfully")
                .build();
    }

    @PostMapping("/reset-password")
    ApiResponse<Void> resetPassword(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        Long userId = jwt.getClaim("userId");
        authService.resetPassword(userId, request);
        return ApiResponse.<Void>builder()
                .message("Reset password successfully")
                .build();
    }

    @PostMapping("/users/{userId}/reset-password")
//    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> resetPasswordForUser(
            @PathVariable Long userId,
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        authService.resetPassword(userId, request);
        return ApiResponse.<Void>builder()
                .message("User password reset successfully")
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(
            @CookieValue(name = TokenConstants.TOKEN) String token
    ) {
        return ApiResponse.<IntrospectResponse>builder()
                .data(authService.introspect(token))
                .build();
    }

}
