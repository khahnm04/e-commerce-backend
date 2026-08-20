package com.khahnm04.ecommerce.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.dto.response.user.AccountAdminResponse;
import lombok.Builder;
import java.io.Serializable;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AdminLoginResponse(

    AccountAdminResponse accountAdmin,
    Long userId,
    String accessToken,
    String refreshToken

) implements Serializable {
}
