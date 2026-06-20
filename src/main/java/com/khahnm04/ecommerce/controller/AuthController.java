package com.khahnm04.ecommerce.controller;

import com.khahnm04.ecommerce.config.Translator;
import com.khahnm04.ecommerce.constant.auth.UserType;
import com.khahnm04.ecommerce.dto.request.auth.LoginRequest;
import com.khahnm04.ecommerce.dto.response.auth.LoginResponse;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.auth.RefreshTokenResponse;
import com.khahnm04.ecommerce.service.auth.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "ADMIN-AUTH-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
@Tag(name = "Admin Auth Controller", description = "APIs for managing auth")
public class AuthController {

    private final AuthService authService;
    private final Translator translator;

    @PostMapping({"/login", "/admin/login"})
    @Operation(summary = "Login")
    public ApiResponse<LoginResponse> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletResponse response
    ) {
        log.info("Login");
        return ApiResponse.<LoginResponse>builder()
            .data(authService.login(request, response))
            .message(translator.toLocale("ADMIN_LOGIN_SUCCESS"))
            .build();
    }

    @PostMapping({"/logout", "/admin/logout"})
    @Operation(summary = "Logout")
    public ApiResponse<Void> logout(
        @CookieValue(name = "access_token", required = false) String accessToken,
        @CookieValue(name = "refresh_token", required = false) String refreshToken,
        HttpServletResponse response
    ) {
        log.info("Logout");
        authService.logout(accessToken, refreshToken, response);
        return ApiResponse.<Void>builder()
            .message(translator.toLocale("ADMIN_LOGOUT_SUCCESS"))
            .build();
    }

    @PostMapping({"/refresh", "/admin/refresh"})
    ApiResponse<RefreshTokenResponse> refresh(
        @CookieValue(name = "refresh_token") String refreshToken,
        @RequestBody UserType userType,
        HttpServletResponse response
    ) {
        log.info("Refresh token");
        return ApiResponse.<RefreshTokenResponse>builder()
            .data(authService.refreshToken(refreshToken, userType, response))
            .message(translator.toLocale("ADMIN_REFRESH_TOKEN_SUCCESS"))
            .build();
    }

}
