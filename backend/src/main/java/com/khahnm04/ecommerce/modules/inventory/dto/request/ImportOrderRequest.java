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
public class ImportOrderRequest implements Serializable {

    @NotNull(message = "IMPORT_ORDER_WAREHOUSE_REQUIRED")
    private Long warehouseId;

    @NotNull(message = "IMPORT_ORDER_SUPPLIER_REQUIRED")
    private Long supplierId;

    @NotBlank(message = "IMPORT_ORDER_CODE_REQUIRED")
    private String code;

    private String notes;

}
