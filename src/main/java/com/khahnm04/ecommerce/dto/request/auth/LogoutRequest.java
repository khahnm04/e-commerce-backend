package com.khahnm04.ecommerce.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import java.io.Serializable;

@Builder
public record LogoutRequest (

    @NotBlank(message = "REFRESH_TOKEN_REQUIRED")
    String refreshToken

) implements Serializable {
}
