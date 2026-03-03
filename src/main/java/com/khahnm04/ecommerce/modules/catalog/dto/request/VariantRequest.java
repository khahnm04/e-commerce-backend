package com.khahnm04.ecommerce.modules.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariantRequest implements Serializable {

    @NotBlank(message = "VARIANT_NAME_REQUIRED")
    private String name;

    @NotBlank(message = "VARIANT_CODE_REQUIRED")
    private String code;

}
