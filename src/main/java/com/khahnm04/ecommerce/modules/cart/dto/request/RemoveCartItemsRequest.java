package com.khahnm04.ecommerce.modules.cart.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoveCartItemsRequest implements Serializable {

    @NotEmpty(message = "CART_ITEM_NOT_FOUND")
    private Set<Long> itemIds;

}
