package com.khahnm04.ecommerce.modules.inventory.dto.request;

import com.khahnm04.ecommerce.modules.inventory.enums.WarehouseStatus;
import com.khahnm04.ecommerce.modules.inventory.enums.WarehouseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseRequest implements Serializable {

    @NotNull(message = "WAREHOUSE_BRANCH_ID_REQUIRED")
    private Long branchId;

    @NotBlank(message = "WAREHOUSE_CODE_REQUIRED")
    private String code;

    @NotBlank(message = "WAREHOUSE_NAME_REQUIRED")
    private String name;

    private WarehouseType type;

    private Integer capacity;

    private WarehouseStatus status;

}
