package com.khahnm18.ecommercebackend.dto.request.brand;

import com.khahnm18.ecommercebackend.constant.brand.BrandStatus;
import jakarta.validation.constraints.NotBlank;

public record BrandRequest(

    @NotBlank(message = "brand.name.required")
    String name,

    @NotBlank(message = "brand.slug.required")
    String slug,

    String avatar,
    String description,
    BrandStatus status

) {}
