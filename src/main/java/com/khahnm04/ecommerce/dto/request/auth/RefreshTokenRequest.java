package com.khahnm04.ecommerce.dto.request.auth;

import com.khahnm04.ecommerce.constant.auth.UserType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import java.io.Serializable;

@Builder
public record RefreshTokenRequest(

    @NotBlank(message = "REFRESH_TOKEN_REQUIRED")
    String refreshToken,

    UserType userType

) implements Serializable {
}
