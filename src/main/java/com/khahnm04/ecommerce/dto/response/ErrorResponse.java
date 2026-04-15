package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(

    LocalDateTime timestamp,
    Boolean success,
    Integer code,
    String message,
    String path,
    String method,
    List<FieldErrorResponse> errors

) implements Serializable {

    public ErrorResponse {
        if (timestamp == null) timestamp = LocalDateTime.now();
        if (success == null) success = false;
    }

}
