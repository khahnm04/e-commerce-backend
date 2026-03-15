package com.khahnm04.ecommerce.modules.cart.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import java.io.Serializable;
import java.util.Set;

@Builder
public record RemoveCartItemsRequest(
        @NotEmpty(message = "CART_ITEM_NOT_FOUND") Set<Long> itemIds) implements Serializable {
}
