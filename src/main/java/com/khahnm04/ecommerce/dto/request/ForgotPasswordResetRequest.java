package com.khahnm04.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ForgotPasswordResetRequest(

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email,

    @NotBlank(message = "Reset token is required")
    String resetToken,

    @NotBlank(message = "New password is required")
    String newPassword,

    @NotBlank(message = "Confirm password is required")
    String confirmPassword
    
) {
}
