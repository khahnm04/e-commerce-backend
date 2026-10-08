package com.khahnm18.ecommercebackend.dto.request.product;

import com.khahnm18.ecommercebackend.entity.product.SpecificationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record ProductVersionRequest(

    @NotNull(message = "product-version.groupId.required")
    UUID groupId,

    @NotBlank(message = "product-version.name.required")
    String name,

    @NotBlank(message = "product-version.slug.required")
    String slug,

    @NotBlank(message = "product-version.version.required")
    String version,

    @NotEmpty(message = "product-version.specifications.required")
    List<SpecificationGroup> specifications,

    String description,
    String content

) {}
