package com.khahnm04.ecommerce.modules.cart.dto.response;

import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;

@Builder
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
        BigDecimal subtotal) implements Serializable {
}
