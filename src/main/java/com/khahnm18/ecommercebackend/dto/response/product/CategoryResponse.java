package com.khahnm18.ecommercebackend.dto.response.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm18.ecommercebackend.enums.CategoryStatus;
import lombok.Builder;
import java.time.Instant;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryResponse(

    UUID id,
    UUID parentId,
    String name,
    String slug,
    String avatar,
    String description,
    CategoryStatus status,
    Instant createdAt,
    UUID createdBy,
    Instant updatedAt,
    UUID updatedBy,
    Instant deletedAt,
    UUID deletedBy,
    Boolean isDeleted

) {}
