package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.config.Translator;
import com.khahnm04.ecommerce.dto.request.auth.LoginRequest;
import com.khahnm04.ecommerce.dto.request.auth.LogoutRequest;
import com.khahnm04.ecommerce.dto.request.auth.RefreshTokenRequest;
import com.khahnm04.ecommerce.dto.response.auth.AdminLoginResponse;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.auth.RefreshTokenResponse;
import com.khahnm04.ecommerce.service.auth.AdminAuthService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "ADMIN-AUTH-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/admin/auth")
@RequiredArgsConstructor
@Tag(name = "Admin Auth Controller", description = "APIs for managing auth")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;
    private final Translator translator;

    @PostMapping("/login")
    @Operation(summary = "Login")
    public ApiResponse<AdminLoginResponse> login(
        @Valid @RequestBody LoginRequest request
    ) {
        log.info("Login");
        return ApiResponse.<AdminLoginResponse>builder()
            .message(translator.toLocale("ADMIN_LOGIN_SUCCESS"))
            .data(adminAuthService.login(request))
            .build();
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout")
    public ApiResponse<Void> logout(
        @Valid @RequestBody LogoutRequest request
    ) {
        log.info("Logout");
        adminAuthService.logout(request);
        return ApiResponse.<Void>builder()
            .message(translator.toLocale("ADMIN_LOGOUT_SUCCESS"))
            .build();
    }

    @PostMapping("/refresh")
    ApiResponse<RefreshTokenResponse> refresh(
        @Valid @RequestBody RefreshTokenRequest request
    ) {
        log.info("Refresh token");
        return ApiResponse.<RefreshTokenResponse>builder()
            .message(translator.toLocale("ADMIN_REFRESH_TOKEN_SUCCESS"))
            .data(adminAuthService.refreshToken(request))
            .build();
    }

}
