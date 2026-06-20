package com.khahnm04.ecommerce.dto.request.brand;

import com.khahnm04.ecommerce.constant.brand.BrandStatus;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

public record BrandRequest(

    @NotBlank(message = "BRAND_NAME_REQUIRED")
    String name,

    @NotBlank(message = "BRAND_SLUG_REQUIRED")
    String slug,

    String logo,
    BrandStatus status

) implements Serializable {
}
