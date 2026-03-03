package com.khahnm04.ecommerce.modules.inventory.dto.request;

import com.khahnm04.ecommerce.modules.inventory.enums.SupplierStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequest implements Serializable {

    @NotBlank(message = "SUPPLIER_CODE_REQUIRED")
    private String code;

    @NotBlank(message = "SUPPLIER_NAME_REQUIRED")
    private String name;

    private String contactPerson;

    private String phone;

    private String email;

    private String address;

    private String taxCode;

    private String paymentTerms;

    private SupplierStatus status;

}
