package com.khahnm04.ecommerce.modules.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest implements Serializable {

    @NotBlank(message = "CATEGORY_NAME_REQUIRED")
    private String name;

    @NotBlank(message = "CATEGORY_SLUG_REQUIRED")
    private String slug;

    private String description;

    private String iconUrl;

    private String bannerUrl;

    private Integer position;

    private String metaTitle;

    private String metaDescription;

    private String metaKeywords;

    private Long parentId;

}
