package com.khahnm18.ecommercebackend.controller.client;

import com.khahnm18.ecommercebackend.annotation.ratelimit.RateLimit;
import com.khahnm18.ecommercebackend.dto.request.auth.LoginRequest;
import com.khahnm18.ecommercebackend.dto.request.auth.LogoutRequest;
import com.khahnm18.ecommercebackend.dto.request.auth.RefreshTokenRequest;
import com.khahnm18.ecommercebackend.dto.request.auth.RegisterRequest;
import com.khahnm18.ecommercebackend.dto.response.ApiResponse;
import com.khahnm18.ecommercebackend.dto.response.auth.LoginResponse;
import com.khahnm18.ecommercebackend.dto.response.auth.RefreshTokenResponse;
import com.khahnm18.ecommercebackend.dto.response.auth.RegisterResponse;
import com.khahnm18.ecommercebackend.service.auth.AuthService;
import com.khahnm18.ecommercebackend.util.Translator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "AUTH-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "Auth management APIs")
public class AuthController {

    private final AuthService authService;
    private final Translator translator;

    @PostMapping("/login")
    @Operation(summary = "Login")
    @RateLimit(keyPrefix = "auth_login", capacity = 5, refillSeconds = 60)
    public ApiResponse<LoginResponse> login(
        @Valid @RequestBody LoginRequest request
    ) {
        log.info("Login client: email={}", request.email());
        return ApiResponse.<LoginResponse>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("auth.login.success"))
            .data(authService.loginClient(request))
            .build();
    }

    @PostMapping("/register")
    @Operation(summary = "Register")
    public ApiResponse<RegisterResponse> register(
        @Valid @RequestBody RegisterRequest request
    ) {
        log.info("Register client: email={}", request.email());
        return ApiResponse.<RegisterResponse>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("auth.register.success"))
            .data(authService.registerClient(request))
            .build();
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout")
    public ApiResponse<Void> logout(
        @Valid @RequestBody LogoutRequest request
    ) {
        log.info("Logout");
        authService.logout(request);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("auth.logout.success"))
            .build();
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token")
    ApiResponse<RefreshTokenResponse> refresh(
        @Valid @RequestBody RefreshTokenRequest request
    ) {
        log.info("Refresh token");
        return ApiResponse.<RefreshTokenResponse>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("auth.refresh.success"))
            .data(authService.refreshToken(request))
            .build();
    }

}
