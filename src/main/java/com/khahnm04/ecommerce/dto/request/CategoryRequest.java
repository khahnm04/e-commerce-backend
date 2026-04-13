package com.khahnm04.ecommerce.dto.request;

import com.khahnm04.ecommerce.common.enums.CategoryStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import java.io.Serializable;

@Builder
public record CategoryRequest(

    @NotBlank(message = "CATEGORY_NAME_REQUIRED")
    String name,

    String slug,
    String description,
    String thumbnail,
    String icon,
    Boolean isFeatured,
    Integer position,
    Long parentId,
    CategoryStatus status

) implements Serializable {
}
