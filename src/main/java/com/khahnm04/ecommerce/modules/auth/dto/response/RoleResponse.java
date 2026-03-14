package com.khahnm04.ecommerce.modules.auth.dto.response;

import lombok.Builder;
import java.util.Set;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record RoleResponse(

    Long id,
    String code,
    String name,
    String description,
    Set<String> permissions,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime updatedAt,
    String updatedBy,
    LocalDateTime deletedAt

) implements Serializable {
}
