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
public class ProductVariantBulkActionRequest implements Serializable {

    @NotEmpty(message = "PRODUCT_VARIANT_SELECTION_REQUIRED")
    private Set<Long> productVariants;

    @NotBlank(message = "PRODUCT_VARIANT_ACTION_REQUIRED")
    private String action;

}
