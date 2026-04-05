package com.khahnm04.ecommerce.dto.response;

import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
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
