package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.io.Serializable;
import java.time.Instant;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(

    Instant timestamp,
    Boolean success,
    String message,
    T data

) implements Serializable {
    public ApiResponse {
        if (timestamp == null) timestamp = Instant.now();
        if (success == null) success = true;
    }
}
