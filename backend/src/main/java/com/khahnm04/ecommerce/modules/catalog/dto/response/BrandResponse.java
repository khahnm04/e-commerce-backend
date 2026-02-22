package com.khahnm04.ecommerce.modules.catalog.dto.response;

import com.khahnm04.ecommerce.common.dto.BaseResponse;
import com.khahnm04.ecommerce.modules.catalog.enums.BrandStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse extends BaseResponse<Long> {

    private String name;
    private String slug;
    private String description;
    private String logoUrl;
    private String country;
    private String websiteUrl;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private BrandStatus status;

}
