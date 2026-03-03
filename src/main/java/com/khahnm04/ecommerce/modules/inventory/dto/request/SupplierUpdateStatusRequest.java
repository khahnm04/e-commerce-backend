package com.khahnm04.ecommerce.modules.inventory.dto.request;

import com.khahnm04.ecommerce.modules.inventory.enums.SupplierStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierUpdateStatusRequest implements Serializable {

    @NotNull(message = "SUPPLIER_STATUS_REQUIRED")
    private SupplierStatus status;

}
