package com.khahnm04.ecommerce.modules.inventory.dto.response;

import com.khahnm04.ecommerce.common.dto.BaseResponse;
import com.khahnm04.ecommerce.modules.inventory.enums.WarehouseStatus;
import com.khahnm04.ecommerce.modules.inventory.enums.WarehouseType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseResponse extends BaseResponse<Long> {

    private Long branchId;
    private String branchName;
    private String code;
    private String name;
    private WarehouseType type;
    private Integer capacity;
    private WarehouseStatus status;

}
