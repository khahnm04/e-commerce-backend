package com.khahnm04.ecommerce.dto.response.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.common.enums.CategoryStatus;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryResponse(

    Long id,
    String name,
    String slug,
    String description,
    String thumbnail,
    Integer position,
    String path,
    Integer level,
    CategoryStatus status,
    Long parentId,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime updatedAt,
    String updatedBy,
    LocalDateTime deletedAt

) implements Serializable {
}
