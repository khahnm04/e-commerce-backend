package com.khahnm04.ecommerce.modules.inventory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryCheckDetailUpdateRequest implements Serializable {

    @NotNull(message = "INVENTORY_CHECK_ACTUAL_QTY_REQUIRED")
    @Min(value = 0, message = "INVENTORY_CHECK_ACTUAL_QTY_MIN")
    private Integer actualQuantity;

    private String reason;

}
