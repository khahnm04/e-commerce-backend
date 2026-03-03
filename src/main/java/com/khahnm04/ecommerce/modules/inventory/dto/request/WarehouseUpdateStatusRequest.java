package com.khahnm04.ecommerce.modules.inventory.dto.request;

import com.khahnm04.ecommerce.modules.inventory.enums.WarehouseStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseUpdateStatusRequest implements Serializable {

    @NotNull(message = "WAREHOUSE_STATUS_REQUIRED")
    private WarehouseStatus status;

}
