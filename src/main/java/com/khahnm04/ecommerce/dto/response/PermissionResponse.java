package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PermissionResponse(

    Long id,
    String code,
    String name,
    String subModule,
    String module,
    String description,
    LocalDateTime createdAt

) implements Serializable {
}
