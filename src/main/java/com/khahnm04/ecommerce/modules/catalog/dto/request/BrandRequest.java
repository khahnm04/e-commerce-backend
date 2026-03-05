package com.khahnm04.ecommerce.modules.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandRequest implements Serializable {

    @NotBlank(message = "BRAND_NAME_REQUIRED")
    private String name;

    @NotBlank(message = "BRAND_SLUG_REQUIRED")
    private String slug;

    private String description;

    private String logoUrl;

    private String country;

    private String websiteUrl;

    private String metaTitle;

    private String metaDescription;

    private String metaKeywords;

}
