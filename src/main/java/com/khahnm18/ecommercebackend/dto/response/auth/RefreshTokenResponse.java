package com.khahnm18.ecommercebackend.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RefreshTokenResponse(

    String accessToken,
    String refreshToken

) {}
