package com.khahnm04.ecommerce.modules.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCartItemRequest implements Serializable {

    @NotNull(message = "CART_ITEM_VARIANT_REQUIRED")
    private Long productVariantId;

    @NotNull(message = "CART_ITEM_QUANTITY_REQUIRED")
    @Min(value = 1, message = "CART_ITEM_QUANTITY_MIN")
    private Integer quantity;

}
