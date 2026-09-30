package com.khahnm18.ecommercebackend.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ChangePasswordRequest(

    @NotBlank(message = "change-password.oldPassword.required")
    String oldPassword,

    @NotBlank(message = "change-password.newPassword.required")
    String newPassword

) {}
