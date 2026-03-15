package com.khahnm04.ecommerce.modules.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.io.Serializable;

@Builder
public record AddCartItemRequest(
        @NotNull(message = "CART_ITEM_VARIANT_REQUIRED") Long productVariantId,

        @NotNull(message = "CART_ITEM_QUANTITY_REQUIRED") @Min(value = 1, message = "CART_ITEM_QUANTITY_MIN") Integer quantity)
        implements Serializable {
}
