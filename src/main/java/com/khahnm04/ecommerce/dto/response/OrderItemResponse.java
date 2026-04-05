package com.khahnm04.ecommerce.dto.response;

import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;

@Builder
public record OrderItemResponse(

    Long productVariantId,
    String productName,
    String sku,
    String image,
    BigDecimal unitPrice,
    Long quantity

) implements Serializable {
}
