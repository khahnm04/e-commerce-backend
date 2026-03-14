package com.khahnm04.ecommerce.modules.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import java.io.Serializable;

@Builder
public record AttributeRequest (

    @NotBlank(message = "ATTRIBUTE_NAME_REQUIRED")
    String name

) implements Serializable {
}
