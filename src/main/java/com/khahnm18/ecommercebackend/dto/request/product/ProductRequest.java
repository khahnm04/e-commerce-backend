package com.khahnm18.ecommercebackend.dto.request.product;

import com.khahnm18.ecommercebackend.enums.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ProductRequest(

    @NotBlank(message = "product.name.required")
    String name,

    @NotBlank(message = "product.slug.required")
    String slug,

    BigDecimal priceOld,
    BigDecimal priceNew,
    Integer discount,
    Integer stock,

    @NotBlank(message = "product.avatar.required")
    String avatar,

    List<String> images,
    Map<String, Object> specifications,
    String description,
    String content,
    ProductStatus status,

    @NotEmpty(message = "product.categories.required")
    List<UUID> categories

) {}
