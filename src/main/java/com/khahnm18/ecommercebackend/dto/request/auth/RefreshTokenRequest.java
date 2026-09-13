package com.khahnm18.ecommercebackend.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RefreshTokenRequest(

    @NotBlank(message = "refresh.token.required")
    String refreshToken

) {}
