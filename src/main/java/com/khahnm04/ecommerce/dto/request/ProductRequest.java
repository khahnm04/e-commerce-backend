package com.khahnm04.ecommerce.dto.request;

import com.khahnm04.ecommerce.common.enums.ProductStatus;
import com.khahnm04.ecommerce.common.enums.ProductVariantStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
public record ProductRequest(

    @NotBlank(message = "PRODUCT_NAME_REQUIRED")
    String name,

    String slug,
    String code,

    @NotNull(message = "PRODUCT_BRAND_REQUIRED")
    Long brandId,

    Integer warrantyMonths,

    @NotNull(message = "PRODUCT_PRICE_REQUIRED")
    BigDecimal price,

    BigDecimal oldPrice,

    @NotBlank(message = "PRODUCT_THUMBNAIL_REQUIRED")
    String thumbnail,

    String description,
    String shortDescription,
    ProductStatus status,
    Boolean isFeatured,
    Boolean isTrending,

    @NotEmpty(message = "PRODUCT_CATEGORIES_REQUIRED")
    Set<Long> categories,

    List<Long> tags,

    @NotEmpty(message = "PRODUCT_IMAGES_REQUIRED")
    List<ProductImageRequest> images,

    List<ProductVariantRequest> productVariants

) implements Serializable {
}
