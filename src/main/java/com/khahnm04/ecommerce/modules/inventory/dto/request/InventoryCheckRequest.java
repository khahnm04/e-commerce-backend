package com.khahnm04.ecommerce.modules.inventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryCheckRequest implements Serializable {

    @NotNull(message = "INVENTORY_CHECK_WAREHOUSE_REQUIRED")
    private Long warehouseId;

    @NotBlank(message = "INVENTORY_CHECK_CODE_REQUIRED")
    private String code;

    @NotBlank(message = "INVENTORY_CHECK_TYPE_REQUIRED")
    private String checkType;

    private String notes;

}
