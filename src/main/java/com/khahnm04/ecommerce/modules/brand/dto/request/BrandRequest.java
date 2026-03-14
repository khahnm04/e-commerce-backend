package com.khahnm04.ecommerce.modules.brand.dto.request;

import com.khahnm04.ecommerce.modules.brand.enums.BrandStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import java.io.Serializable;

@Builder
public record BrandRequest(

    @NotBlank(message = "BRAND_NAME_REQUIRED")
    String name,

    String slug,
    String description,
    String logo,
    String country,
    String website,
    BrandStatus status

) implements Serializable {
}
