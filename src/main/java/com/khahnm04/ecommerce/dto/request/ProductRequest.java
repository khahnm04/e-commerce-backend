package com.khahnm04.ecommerce.dto.request;

import com.khahnm04.ecommerce.common.enums.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
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

    @NotEmpty(message = "PRODUCT_CATEGORIES_REQUIRED")
    Set<Long> categories,

    @NotEmpty(message = "PRODUCT_ATTRIBUTES_REQUIRED")
    List<ProductAttributeValueRequest> attributes,

    @NotEmpty(message = "PRODUCT_IMAGES_REQUIRED")
    List<ProductImageRequest> images

) implements Serializable {

    @Builder
    public record ProductAttributeValueRequest(
        Long productAttributeValueId,

        @NotNull(message = "PRODUCT_ATTRIBUTE_ID_REQUIRED")
        Long attributeId,

        @NotBlank(message = "PRODUCT_ATTRIBUTE_VALUE_REQUIRED")
        String value
    ) implements Serializable {
    }

    @Builder
    public record ProductImageRequest(
        Long productImageId,

        @NotBlank(message = "PRODUCT_IMAGE_URL_REQUIRED")
        String image,

        Integer position
    ) implements Serializable {
    }

}
