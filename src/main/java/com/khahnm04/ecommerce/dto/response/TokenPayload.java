package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.io.Serializable;
import java.util.Date;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TokenPayload(

    String jwtId,
    String token,
    Date expiredTime

) implements Serializable {
}
