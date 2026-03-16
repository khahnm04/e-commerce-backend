package com.khahnm04.ecommerce.modules.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.io.Serializable;

@Builder
public record OrderItemRequest(

    @NotNull(message = "Product variant ID is required")
    Long productVariantId,

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    Long quantity

) implements Serializable {
}
