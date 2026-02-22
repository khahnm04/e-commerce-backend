package com.khahnm04.ecommerce.modules.inventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseBulkActionRequest implements Serializable {

    @NotEmpty(message = "WAREHOUSE_SELECTION_REQUIRED")
    private Set<Long> warehouses;

    @NotBlank(message = "WAREHOUSE_ACTION_REQUIRED")
    private String action;

}
