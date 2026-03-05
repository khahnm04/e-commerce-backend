package com.khahnm04.ecommerce.modules.catalog.dto.request;

import com.khahnm04.ecommerce.modules.catalog.enums.ProductVariantStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantRequest implements Serializable {

    @NotNull(message = "PRODUCT_VARIANT_PRODUCT_ID_REQUIRED")
    private Long productId;

    private String sku;

    @NotBlank(message = "PRODUCT_VARIANT_NAME_REQUIRED")
    private String name;

    @NotNull(message = "PRODUCT_VARIANT_PRICE_REQUIRED")
    private BigDecimal price;

    private BigDecimal oldPrice;

    private String image;

    private ProductVariantStatus status;

    private Set<Long> variantValues;

}
