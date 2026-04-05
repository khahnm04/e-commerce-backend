package com.khahnm04.ecommerce.dto.response;

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
