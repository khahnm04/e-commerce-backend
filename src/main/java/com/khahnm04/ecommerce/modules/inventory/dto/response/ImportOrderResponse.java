package com.khahnm04.ecommerce.modules.inventory.dto.response;

import com.khahnm04.ecommerce.common.dto.BaseResponse;
import com.khahnm04.ecommerce.modules.inventory.enums.ImportOrderStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ImportOrderResponse extends BaseResponse<Long> {

    private String code;
    private Long warehouseId;
    private String warehouseName;
    private Long supplierId;
    private String supplierName;
    private LocalDateTime importDate;
    private Integer totalQuantity;
    private BigDecimal totalAmount;
    private ImportOrderStatus status;
    private String notes;
    private List<ImportDetailResponse> details;

}
