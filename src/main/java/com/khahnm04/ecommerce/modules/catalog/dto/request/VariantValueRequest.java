package com.khahnm04.ecommerce.modules.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariantValueRequest implements Serializable {

    @NotNull(message = "VARIANT_VALUE_VARIANT_ID_REQUIRED")
    private Long variantId;

    private String code;

    @NotBlank(message = "VARIANT_VALUE_REQUIRED")
    private String value;

    private Integer displayOrder;

}
