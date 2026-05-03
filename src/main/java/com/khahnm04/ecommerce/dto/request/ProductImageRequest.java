package com.khahnm04.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record ProductImageRequest(
    Long productImageId,

    @NotBlank(message = "PRODUCT_IMAGE_URL_REQUIRED")
    String image,

    Integer position
) implements Serializable {
}
