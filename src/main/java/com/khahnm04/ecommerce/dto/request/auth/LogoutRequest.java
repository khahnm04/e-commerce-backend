package com.khahnm04.ecommerce.dto.request.auth;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record LogoutRequest (

    String refreshToken

) implements Serializable {
}
