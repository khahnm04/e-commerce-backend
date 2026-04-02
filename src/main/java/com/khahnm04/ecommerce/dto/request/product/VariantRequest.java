package com.khahnm04.ecommerce.dto.request.product;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import java.io.Serializable;

@Builder
public record VariantRequest(

    @NotBlank(message = "VARIANT_NAME_REQUIRED")
    String name

) implements Serializable {
}
