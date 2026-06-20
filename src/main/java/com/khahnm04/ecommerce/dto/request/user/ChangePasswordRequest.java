package com.khahnm04.ecommerce.dto.request.user;

import com.khahnm04.ecommerce.validation.password.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import java.io.Serializable;

@Builder(toBuilder = true)
public record ChangePasswordRequest(

    @ValidPassword
    @NotBlank(message = "ACCOUNT_ADMIN_PASSWORD_REQUIRED")
    String password

) implements Serializable {
}
