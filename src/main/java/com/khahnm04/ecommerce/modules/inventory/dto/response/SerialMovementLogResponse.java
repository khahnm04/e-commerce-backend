package com.khahnm04.ecommerce.modules.inventory.dto.response;

import com.khahnm04.ecommerce.modules.inventory.enums.SerialMovementType;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SerialMovementLogResponse implements Serializable {

    private Long id;
    private SerialMovementType movementType;
    private Long fromWarehouseId;
    private String fromWarehouseName;
    private Long toWarehouseId;
    private String toWarehouseName;
    private String referenceType;
    private Long referenceId;
    private LocalDateTime movementDate;
    private String notes;
    private LocalDateTime createdAt;
    private String createdByName;

}
