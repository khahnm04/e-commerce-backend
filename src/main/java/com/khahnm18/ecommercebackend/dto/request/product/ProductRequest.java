package com.khahnm18.ecommercebackend.dto.request.product;

import com.khahnm18.ecommercebackend.enums.ProductStatus;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequest(

    @NotNull(message = "product.versionId.required")
    UUID versionId,

    @NotBlank(message = "product.name.required")
    String name,

    @NotBlank(message = "product.slug.required")
    String slug,

    @NotBlank(message = "product.color.required")
    String color,

    String sku,

    Boolean isDefault,

    @DecimalMin(value = "0.00", message = "product.priceOld.invalid")
    BigDecimal priceOld,

    @DecimalMin(value = "0.00", message = "product.priceNew.invalid")
    BigDecimal priceNew,

    @PositiveOrZero(message = "product.stock.invalid")
    Integer stock,

    @NotBlank(message = "product.avatar.required")
    String avatar,

    ProductStatus status

) {}
