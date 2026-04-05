package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.common.enums.ProductVariantStatus;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductVariantResponse(

    Long id,
    String product,
    String sku,
    String name,
    BigDecimal price,
    BigDecimal oldPrice,
    String image,
    ProductVariantStatus status,
    Integer quantity,
    List<VariantValueInfo> variantValues,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime updatedAt,
    String updatedBy,
    LocalDateTime deletedAt

) implements Serializable {

    @Builder
    public record VariantValueInfo(
        Long id,
        String variant,
        String value
    ) implements Serializable {
    }

}
