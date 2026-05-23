package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.io.Serializable;
import java.time.Instant;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(

    Instant timestamp,
    Boolean success,
    String code,
    String message,
    String path

) implements Serializable {
    public ErrorResponse {
        if (timestamp == null) timestamp = Instant.now();
        if (success == null) success = false;
    }
}
