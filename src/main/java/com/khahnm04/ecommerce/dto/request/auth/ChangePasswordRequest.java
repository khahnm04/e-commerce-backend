package com.khahnm04.ecommerce.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import java.io.Serializable;

@Builder
public record ChangePasswordRequest(

    @NotBlank(message = "PASSWORD_REQUIRED")
    String currentPassword,

    @NotBlank(message = "PASSWORD_REQUIRED")
    @Size(min = 8, message = "PASSWORD_MIN_LENGTH")
    String newPassword,

    @NotBlank(message = "PASSWORD_REQUIRED")
    String confirmNewPassword

) implements Serializable {
}
