package com.khahnm04.ecommerce.modules.inventory.dto.response;

import com.khahnm04.ecommerce.common.dto.BaseResponse;
import com.khahnm04.ecommerce.modules.inventory.enums.TransferOrderStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TransferOrderResponse extends BaseResponse<Long> {

    private String code;
    private Long fromWarehouseId;
    private String fromWarehouseName;
    private Long toWarehouseId;
    private String toWarehouseName;
    private LocalDateTime transferDate;
    private Integer totalQuantity;
    private TransferOrderStatus status;
    private String shippingMethod;
    private LocalDateTime receivedDate;
    private String notes;
    private List<TransferDetailResponse> details;

}
