package com.khahnm18.ecommercebackend.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RegisterRequest(

    @NotBlank(message = "register.fullName.required")
    String fullName,

    @NotBlank(message = "register.email.required")
    String email,

    @NotBlank(message = "register.phoneNumber.required")
    String phoneNumber,

    @NotBlank(message = "register.password.required")
    String password

) {}
