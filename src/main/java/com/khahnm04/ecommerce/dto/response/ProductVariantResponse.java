package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.common.enums.ProductVariantStatus;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductVariantResponse(
    Long id,
    Long productId,
    String product,
    String sku,
    String name,
    BigDecimal price,
    BigDecimal oldPrice,
    String image,
    ProductVariantStatus status,
    Integer quantity,
    Map<String, Object> attributes,
    List<VariantValueResponse> values,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime updatedAt,
    String updatedBy,
    LocalDateTime deletedAt,
    String deletedBy
) implements Serializable {
}
