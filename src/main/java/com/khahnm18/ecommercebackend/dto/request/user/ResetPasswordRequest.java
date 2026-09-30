package com.khahnm18.ecommercebackend.dto.request.user;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(

    @NotBlank(message = "reset-password.password.required")
    String password

) {}
