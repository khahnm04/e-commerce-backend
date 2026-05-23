package com.khahnm04.ecommerce.dto.response.auth;

import lombok.Builder;
import java.io.Serializable;
import java.util.Date;

@Builder
public record TokenResponse(

    String jwtId,
    String token,
    Date expiredTime

) implements Serializable {
}
