package com.khahnm04.ecommerce.dto.request.auth;

import com.khahnm04.ecommerce.validation.email.ValidEmail;
import com.khahnm04.ecommerce.validation.password.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import java.io.Serializable;

@Builder
public record AdminLoginRequest(

    @ValidEmail
    @NotBlank(message = "LOGIN_EMAIL_REQUIRED")
    String email,

    @ValidPassword
    @NotBlank(message = "LOGIN_PASSWORD_REQUIRED")
    String password,

    Boolean rememberMe

) implements Serializable {
    public AdminLoginRequest {
        if (rememberMe == null) rememberMe = false;
    }
}
