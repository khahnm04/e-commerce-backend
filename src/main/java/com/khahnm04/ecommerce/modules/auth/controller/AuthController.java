package com.khahnm04.ecommerce.modules.auth.controller;

import com.khahnm04.ecommerce.common.constant.TokenConstants;
import com.khahnm04.ecommerce.modules.auth.dto.request.ChangePasswordRequest;
import com.khahnm04.ecommerce.modules.auth.dto.request.LoginRequest;
import com.khahnm04.ecommerce.modules.auth.dto.response.IntrospectResponse;
import com.khahnm04.ecommerce.modules.auth.dto.response.LoginResponse;
import com.khahnm04.ecommerce.modules.auth.service.AuthService;
import com.khahnm04.ecommerce.common.base.dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/logout")
    ApiResponse<Void> logout(
            @CookieValue(name = TokenConstants.ACCESS_TOKEN) String accessToken,
            @CookieValue(name = TokenConstants.REFRESH_TOKEN) String refreshToken,
            HttpServletResponse response
    ) {
        authService.logout(accessToken, refreshToken, response);
        return ApiResponse.<Void>builder()
                .message("Logout successfully")
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<Void> refresh(
            @CookieValue(name = TokenConstants.REFRESH_TOKEN) String token,
            HttpServletResponse response
    ) {
        authService.refreshToken(token, response);
        return ApiResponse.<Void>builder()
                .message("Refresh token successfully")
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

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(
            @CookieValue(name = TokenConstants.ACCESS_TOKEN) String accessToken
    ) {
        return ApiResponse.<IntrospectResponse>builder()
                .data(authService.introspect(accessToken))
                .build();
    }

}
