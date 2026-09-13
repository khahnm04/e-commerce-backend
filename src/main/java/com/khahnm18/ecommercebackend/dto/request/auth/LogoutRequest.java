package com.khahnm18.ecommercebackend.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LogoutRequest(

    @NotBlank(message = "logout.token.required")
    String refreshToken

) {}
