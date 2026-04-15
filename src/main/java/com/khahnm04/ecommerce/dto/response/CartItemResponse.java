package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CartItemResponse(

    Long id,
    Long productVariantId,
    String productName,
    String productVariantName,
    String sku,
    String image,
    BigDecimal price,
    BigDecimal oldPrice,
    Integer quantity,
    BigDecimal subtotal

) implements Serializable {
}
