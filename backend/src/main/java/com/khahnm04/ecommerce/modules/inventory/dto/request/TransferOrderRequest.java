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
public class TransferOrderRequest implements Serializable {

    @NotNull(message = "TRANSFER_ORDER_FROM_WAREHOUSE_REQUIRED")
    private Long fromWarehouseId;

    @NotNull(message = "TRANSFER_ORDER_TO_WAREHOUSE_REQUIRED")
    private Long toWarehouseId;

    @NotBlank(message = "TRANSFER_ORDER_CODE_REQUIRED")
    private String code;

    private String shippingMethod;

    private String notes;

}
