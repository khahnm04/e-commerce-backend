package com.khahnm18.ecommercebackend.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginRequest(

    @NotBlank(message = "login.email.required")
    String email,

    @NotBlank(message = "login.password.required")
    String password

) {}
