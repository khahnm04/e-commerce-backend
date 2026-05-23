package com.khahnm04.ecommerce.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.io.Serializable;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RefreshTokenResponse(

    String accessToken,
    String refreshToken

) implements Serializable {
}
