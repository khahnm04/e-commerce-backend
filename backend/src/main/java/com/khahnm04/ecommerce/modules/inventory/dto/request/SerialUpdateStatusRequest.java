package com.khahnm04.ecommerce.modules.inventory.dto.request;

import com.khahnm04.ecommerce.modules.inventory.enums.SerialNumberStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SerialUpdateStatusRequest implements Serializable {

    @NotNull(message = "SERIAL_STATUS_REQUIRED")
    private SerialNumberStatus status;

    private String notes;

}
