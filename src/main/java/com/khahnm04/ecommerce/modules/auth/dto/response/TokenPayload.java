package com.khahnm04.ecommerce.modules.auth.dto.response;

import lombok.Builder;
import java.io.Serializable;
import java.util.Date;

@Builder
public record TokenPayload(

    String jwtId,
    String token,
    Date expiredTime

) implements Serializable {
}
