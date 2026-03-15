package com.khahnm04.ecommerce.modules.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariantValueBulkActionRequest implements Serializable {

    @NotEmpty(message = "VARIANT_VALUE_SELECTION_REQUIRED")
    private Set<Long> variantValues;

    @NotBlank(message = "VARIANT_VALUE_ACTION_REQUIRED")
    private String action;

}
