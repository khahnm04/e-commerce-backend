package com.khahnm04.ecommerce.modules.inventory.dto.response;

import com.khahnm04.ecommerce.common.dto.BaseResponse;
import com.khahnm04.ecommerce.modules.inventory.enums.SupplierStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierResponse extends BaseResponse<Long> {

    private String code;
    private String name;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private String taxCode;
    private String paymentTerms;
    private SupplierStatus status;

}
