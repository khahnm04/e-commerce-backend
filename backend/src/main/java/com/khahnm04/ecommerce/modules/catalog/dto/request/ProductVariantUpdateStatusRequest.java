package com.khahnm04.ecommerce.modules.catalog.dto.request;

import com.khahnm04.ecommerce.modules.catalog.enums.ProductVariantStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantUpdateStatusRequest implements Serializable {

    @NotNull(message = "PRODUCT_VARIANT_STATUS_REQUIRED")
    private ProductVariantStatus status;

}
