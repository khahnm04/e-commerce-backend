package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.common.enums.ProductStatus;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    List<ProductTagResponse> tags,
    List<ProductImageResponse> images,
    List<ProductVariantResponse> productVariants,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime updatedAt,
    String updatedBy,
    LocalDateTime deletedAt,
    String deletedBy
) implements Serializable {
}
