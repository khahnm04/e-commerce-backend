package com.khahnm04.ecommerce.dto.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.io.Serializable;

@Builder
public record VariantValueRequest (

    @NotNull(message = "VARIANT_VALUE_VARIANT_ID_REQUIRED")
    Long variantId,

    @NotBlank(message = "VARIANT_VALUE_REQUIRED")
    String value

) implements Serializable {
}
