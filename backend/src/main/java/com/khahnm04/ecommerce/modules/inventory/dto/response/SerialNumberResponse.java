package com.khahnm04.ecommerce.modules.inventory.dto.response;

import com.khahnm04.ecommerce.common.dto.BaseResponse;
import com.khahnm04.ecommerce.modules.inventory.enums.SerialNumberStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SerialNumberResponse extends BaseResponse<Long> {

    private String serialCode;
    private Long productVariantId;
    private String productVariantSku;
    private String productVariantName;
    private Long warehouseId;
    private String warehouseName;
    private SerialNumberStatus status;
    private BigDecimal purchasePrice;
    private BigDecimal soldPrice;
    private LocalDateTime soldDate;
    private LocalDate warrantyStartDate;
    private LocalDate warrantyEndDate;
    private String notes;

}
