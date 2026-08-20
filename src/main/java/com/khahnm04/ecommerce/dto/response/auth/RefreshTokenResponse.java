package com.khahnm04.ecommerce.dto.response.auth;

import lombok.Builder;

@Builder
public record RefreshTokenResponse(

    String accessToken,
    String refreshToken

) {}
