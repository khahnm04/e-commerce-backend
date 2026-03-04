package com.khahnm04.ecommerce.modules.catalog.dto.response;

import com.khahnm04.ecommerce.common.dto.BaseResponse;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse extends BaseResponse<Long> {

    private String name;
    private String slug;
    private String description;
    private String iconUrl;
    private String bannerUrl;
    private Long parentId;
    private String parentName;
    private Integer position;
    private Integer level;
    private String path;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;

}
