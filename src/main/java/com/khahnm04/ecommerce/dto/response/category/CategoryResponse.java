package com.khahnm04.ecommerce.dto.response.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.constant.category.CategoryStatus;
import lombok.Builder;
import java.io.Serializable;
import java.time.Instant;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryResponse(

    Long id,
    Long parentId,
    String name,
    String slug,
    String thumbnail,
    Boolean isFeatured,
    Boolean showInMenu,
    CategoryStatus status,
    Instant createdAt,
    String createdBy,
    Instant updatedAt,
    String updatedBy,
    Instant deletedAt,
    String deletedBy,
    Boolean isDeleted

) implements Serializable {
}
