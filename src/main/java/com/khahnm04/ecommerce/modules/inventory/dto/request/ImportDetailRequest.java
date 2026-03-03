package com.khahnm04.ecommerce.modules.inventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportDetailRequest implements Serializable {

    @NotNull(message = "IMPORT_DETAIL_VARIANT_REQUIRED")
    private Long productVariantId;

    @NotBlank(message = "IMPORT_DETAIL_SERIAL_CODE_REQUIRED")
    private String serialCode;

    @NotNull(message = "IMPORT_DETAIL_UNIT_PRICE_REQUIRED")
    private BigDecimal unitPrice;

}
