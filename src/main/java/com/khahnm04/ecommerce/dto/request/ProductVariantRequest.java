package com.khahnm04.ecommerce.dto.request;

import com.khahnm04.ecommerce.common.enums.ProductVariantStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Builder
public record ProductVariantRequest(
    Long id,

    @NotBlank(message = "PRODUCT_VARIANT_SKU_REQUIRED")
    String sku,

    @NotBlank(message = "PRODUCT_VARIANT_NAME_REQUIRED")
    String name,

    @NotNull(message = "PRODUCT_VARIANT_PRICE_REQUIRED")
    BigDecimal price,

    BigDecimal oldPrice,
    Integer quantity,
    ProductVariantStatus status,
    String image,
    Map<String, Object> attributes,

    @NotEmpty(message = "PRODUCT_VARIANT_VALUES_REQUIRED")
    List<Long> variantValueIds
) implements Serializable {
}
