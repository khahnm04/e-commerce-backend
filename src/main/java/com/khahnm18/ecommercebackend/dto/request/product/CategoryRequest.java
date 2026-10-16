package com.khahnm18.ecommercebackend.dto.request.product;

import com.khahnm18.ecommercebackend.enums.CategoryStatus;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record CategoryRequest(

    UUID parentId,

    @NotBlank(message = "category.name.required")
    String name,

    @NotBlank(message = "category.slug.required")
    String slug,

    String avatar,
    String description,
    CategoryStatus status

) {}
