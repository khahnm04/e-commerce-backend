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
public class ProductBulkActionRequest implements Serializable {

    @NotEmpty(message = "PRODUCT_SELECTION_REQUIRED")
    private Set<Long> products;

    @NotBlank(message = "PRODUCT_ACTION_REQUIRED")
    private String action;

}
