package com.khahnm04.ecommerce.modules.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest implements Serializable {

    @NotBlank(message = "PASSWORD_REQUIRED")
    private String currentPassword;

    @NotBlank(message = "PASSWORD_REQUIRED")
    @Size(min = 8, message = "PASSWORD_MIN_LENGTH")
    private String newPassword;

    @NotBlank(message = "PASSWORD_REQUIRED")
    private String confirmNewPassword;

}
