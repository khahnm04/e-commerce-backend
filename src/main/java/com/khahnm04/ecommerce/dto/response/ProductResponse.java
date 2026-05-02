package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.common.enums.ProductStatus;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductResponse(

    Long id,
    String name,
    String slug,
    String code,
    String brandName,
    Long brandId,
    Integer warrantyMonths,
    BigDecimal price,
    BigDecimal oldPrice,
    String thumbnail,
    String description,
    String shortDescription,
    ProductStatus status,
    Boolean isFeatured,
    Boolean isTrending,
    List<ProductCategoryResponse> categories,
    List<ProductAttributeValueResponse> attributes,
    List<ProductImageResponse> images,
    List<ProductVariantResponse> variants,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime updatedAt,
    String updatedBy,
    LocalDateTime deletedAt,
    String deletedBy

) implements Serializable {

    @Builder(toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ProductCategoryResponse(
        Long productCategoryId,
        Long categoryId,
        String categoryName
    ) implements Serializable {
    }

    @Builder(toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ProductAttributeValueResponse(
        Long productAttributeValueId,
        Long attributeId,
        String attributeName,
        String value
    ) implements Serializable {
    }

    @Builder(toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ProductImageResponse(
        Long productImageId,
        String image,
        Integer position
    ) implements Serializable {
    }

    @Builder(toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ProductVariantResponse(
        Long id,
        String sku,
        String name,
        BigDecimal price,
        BigDecimal oldPrice,
        Integer quantity,
        com.khahnm04.ecommerce.common.enums.ProductVariantStatus status,
        String image,
        List<VariantValueResponse> values
    ) implements Serializable {
    }

    @Builder(toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record VariantValueResponse(
        Long id,
        Long variantId,
        String variantName,
        String value
    ) implements Serializable {
    }

}
