package com.khahnm04.ecommerce.dto.response.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.dto.response.permission.PermissionResponse;
import lombok.Builder;
import java.time.Instant;
import java.util.Set;
import java.io.Serializable;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RoleResponse(

    Long id,
    String code,
    String name,
    Set<PermissionResponse> permissions,
    Instant createdAt,
    String createdBy,
    Instant updatedAt,
    String updatedBy,
    Instant deletedAt,
    String deletedBy,
    Boolean isDeleted

) implements Serializable {
}
