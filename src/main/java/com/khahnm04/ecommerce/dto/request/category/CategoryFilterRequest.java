package com.khahnm04.ecommerce.dto.request.category;

import com.khahnm04.ecommerce.constant.category.CategoryStatus;
import lombok.Builder;
import java.io.Serializable;
import java.time.Instant;

@Builder
public record CategoryFilterRequest (

    Integer page,
    Integer size,
    Boolean isDeleted,
    String sort,
    String keyword,
    Instant startDate,
    Instant endDate,
    CategoryStatus status,
    Long parentId,
    Boolean isFeatured,
    Boolean showInMenu

) implements Serializable {
    public CategoryFilterRequest {
        if (page == null) page = 1;
        if (size == null) size = 10;
        if (sort == null) sort = "createdAt,desc";
        if (isDeleted == null) isDeleted = false;
    }
}
