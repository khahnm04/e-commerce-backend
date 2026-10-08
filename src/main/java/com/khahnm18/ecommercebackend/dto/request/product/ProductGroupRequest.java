package com.khahnm18.ecommercebackend.dto.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record ProductGroupRequest(

    @NotBlank(message = "product-group.name.required")
    String name,

    @NotBlank(message = "product-group.slug.required")
    String slug,

    @Size(max = 20, message = "product-group.images.max")
    @NotEmpty(message = "product-group.images.required")
    List<String> images,

    @NotEmpty(message = "product-group.categories.required")
    List<UUID> categories

) {}
