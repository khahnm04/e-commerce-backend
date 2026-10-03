package com.khahnm18.ecommercebackend.dto.response.brand;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm18.ecommercebackend.constant.brand.BrandStatus;
import lombok.Builder;
import java.time.Instant;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BrandResponse(

    UUID id,
    String name,
    String slug,
    String avatar,
    String description,
    BrandStatus status,
    Instant createdAt,
    UUID createdBy,
    Instant updatedAt,
    UUID updatedBy,
    Instant deletedAt,
    UUID deletedBy,
    Boolean isDeleted

) {}
