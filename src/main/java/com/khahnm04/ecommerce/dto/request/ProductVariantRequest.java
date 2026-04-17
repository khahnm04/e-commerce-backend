package com.khahnm04.ecommerce.dto.request;

import com.khahnm04.ecommerce.common.enums.ProductVariantStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Builder
public record ProductVariantRequest(

    Long productId,

    String sku,

    @NotBlank(message = "PRODUCT_VARIANT_NAME_REQUIRED")
    String name,

    @NotNull(message = "PRODUCT_VARIANT_PRICE_REQUIRED")
    BigDecimal price,

    BigDecimal oldPrice,
    String image,
    ProductVariantStatus status,
    Set<Long> variantValues,
    Integer quantity

) implements Serializable {
}
