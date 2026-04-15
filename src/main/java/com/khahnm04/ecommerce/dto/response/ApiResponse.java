package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(

    LocalDateTime timestamp,
    Boolean success,
    String message,
    PageMetaResponse meta,
    T data

) implements Serializable {

    public ApiResponse {
        if (timestamp == null) timestamp = LocalDateTime.now();
        if (success == null) success = true;
    }

}
