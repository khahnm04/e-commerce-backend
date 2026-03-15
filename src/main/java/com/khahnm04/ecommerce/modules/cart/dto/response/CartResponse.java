package com.khahnm04.ecommerce.modules.cart.dto.response;

import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder
public record CartResponse(
        Long id,
        List<CartItemResponse> items,
        int totalItems,
        BigDecimal totalAmount) implements Serializable {
}
