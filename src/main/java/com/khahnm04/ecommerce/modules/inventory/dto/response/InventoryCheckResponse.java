package com.khahnm04.ecommerce.modules.inventory.dto.response;

import com.khahnm04.ecommerce.modules.inventory.enums.InventoryCheckStatus;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryCheckResponse implements Serializable {

    private Long id;
    private String code;
    private Long warehouseId;
    private String warehouseName;
    private LocalDateTime checkDate;
    private String checkType;
    private Integer systemQuantity;
    private Integer actualQuantity;
    private Integer difference;
    private InventoryCheckStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<InventoryCheckDetailResponse> details;

}
