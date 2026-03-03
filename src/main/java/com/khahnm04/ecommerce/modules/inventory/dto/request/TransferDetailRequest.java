package com.khahnm04.ecommerce.modules.inventory.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferDetailRequest implements Serializable {

    @NotNull(message = "TRANSFER_DETAIL_SERIAL_REQUIRED")
    private Long serialNumberId;

}
