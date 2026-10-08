package com.khahnm18.ecommercebackend.dto.response.product;

import com.khahnm18.ecommercebackend.dto.response.category.CategoryResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductGroupResponse(

    UUID id,
    String name,
    String slug,
    List<String> images,
    List<CategoryResponse> categories,
    Instant createdAt,
    UUID createdBy,
    Instant updatedAt,
    UUID updatedBy,
    Instant deletedAt,
    UUID deletedBy,
    Boolean isDeleted

) {}
