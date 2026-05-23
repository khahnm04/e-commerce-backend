package com.khahnm04.ecommerce.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.io.Serializable;
import java.time.Instant;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PermissionResponse (

    Long id,
    String code,
    String name,
    String module,
    String description,
    Instant createdAt,
    String createdBy,
    Instant updatedAt,
    String updatedBy,
    Instant deletedAt,
    String deletedBy

) implements Serializable {
}
