package com.khahnm18.ecommercebackend.dto.response.auth;

import lombok.Builder;

@Builder
public record RefreshTokenResponse(

    String accessToken,
    String refreshToken

) {}
