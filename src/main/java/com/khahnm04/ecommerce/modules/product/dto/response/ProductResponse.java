package com.khahnm04.ecommerce.modules.product.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.modules.product.enums.ProductStatus;
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
    List<ProductCategoryResponse> categories,
    List<ProductAttributeValueResponse> attributes,
    List<ProductImageResponse> images,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime updatedAt,
    String updatedBy,
    LocalDateTime deletedAt

) implements Serializable {

    @Builder
    public record ProductCategoryResponse(
        Long productCategoryId,
        Long categoryId,
        String categoryName
    ) implements Serializable {
    }

    @Builder
    public record ProductAttributeValueResponse(
        Long productAttributeValueId,
        Long attributeId,
        String attributeName,
        String value
    ) implements Serializable {
    }

    @Builder
    public record ProductImageResponse(
        Long productImageId,
        String image,
        Integer position
    ) implements Serializable {
    }

}
