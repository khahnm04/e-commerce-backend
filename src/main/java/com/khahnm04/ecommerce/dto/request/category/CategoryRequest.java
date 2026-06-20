package com.khahnm04.ecommerce.dto.request.category;

import com.khahnm04.ecommerce.constant.category.CategoryStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import java.io.Serializable;

@Builder
public record CategoryRequest (

    @NotBlank(message = "CATEGORY_NAME_REQUIRED")
    String name,

    @NotBlank(message = "CATEGORY_SLUG_REQUIRED")
    String slug,

    String thumbnail,
    Boolean isFeatured,
    Boolean showInMenu,
    CategoryStatus status,
    Long parentId

) implements Serializable {
}
