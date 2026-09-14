package com.khahnm18.ecommercebackend.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RoleResponse(

    UUID id,
    String code,
    String name,
    List<PermissionResponse> permissions,
    Instant createdAt,
    UUID createdBy,
    Instant updatedAt,
    UUID updatedBy,
    Instant deletedAt,
    UUID deletedBy,
    Boolean isDeleted

) {}
