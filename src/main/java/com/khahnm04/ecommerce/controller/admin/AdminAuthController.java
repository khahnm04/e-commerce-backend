package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.config.Translator;
import com.khahnm04.ecommerce.dto.request.auth.AdminLoginRequest;
import com.khahnm04.ecommerce.dto.request.auth.LogoutRequest;
import com.khahnm04.ecommerce.dto.request.auth.RefreshTokenRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.auth.AdminLoginResponse;
import com.khahnm04.ecommerce.dto.response.auth.IntrospectResponse;
import com.khahnm04.ecommerce.dto.response.auth.RefreshTokenResponse;
import com.khahnm04.ecommerce.service.auth.AdminAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "ADMIN-AUTH-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/admin/auth")
@RequiredArgsConstructor
@Tag(name = "Admin Auth Controller", description = "APIs for managing auth")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;
    private final Translator translator;

    @PostMapping("/login")
    public ApiResponse<AdminLoginResponse> login(
        @Valid @RequestBody AdminLoginRequest request
    ) {
        log.info("Employee login");
        return ApiResponse.<AdminLoginResponse>builder()
            .data(adminAuthService.login(request))
            .message(translator.toLocale("ADMIN_LOGIN_SUCCESS"))
            .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(
        @Valid @RequestBody LogoutRequest request
    ) {
        log.info("Employee logout");
        adminAuthService.logout(request);
        return ApiResponse.<Void>builder()
            .message(translator.toLocale("ADMIN_LOGOUT_SUCCESS"))
            .build();
    }

    @PostMapping("/refresh")
    ApiResponse<RefreshTokenResponse> refresh(
        @Valid @RequestBody RefreshTokenRequest request
    ) {
        log.info("Employee refresh token");
        return ApiResponse.<RefreshTokenResponse>builder()
            .data(adminAuthService.refreshToken(request))
            .message(translator.toLocale("ADMIN_REFRESH_TOKEN_SUCCESS"))
            .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(
        @AuthenticationPrincipal Jwt jwt
    ) {
        log.info("Introspect access token");
        String accessToken = jwt.getTokenValue();
        return ApiResponse.<IntrospectResponse>builder()
            .data(adminAuthService.introspect(accessToken))
            .message(translator.toLocale("ADMIN_INTROSPECT_SUCCESS"))
            .build();
    }

}
