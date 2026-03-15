package com.khahnm04.ecommerce.modules.catalog.dto.response;

import com.khahnm04.ecommerce.common.dto.BaseResponse;
import com.khahnm04.ecommerce.modules.catalog.enums.ProductStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse extends BaseResponse<Long> {

    private String name;
    private String slug;
    private String code;
    private String brandName;
    private Long brandId;
    private Boolean hasSerialNumber;
    private Integer warrantyMonths;
    private BigDecimal price;
    private BigDecimal oldPrice;
    private String thumbnail;
    private String description;
    private String shortDescription;
    private String metaTitle;
    private String metaDescription;
    private ProductStatus status;
    private List<ProductAttributeValueResponse> attributes;
    private List<ProductCategoryResponse> categories;
    private List<ProductImageResponse> images;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductAttributeValueResponse {
        private Long id;
        private Long attributeId;
        private String attributeName;
        private String value;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductCategoryResponse {
        private Long id;
        private Long categoryId;
        private String categoryName;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductImageResponse {
        private Long id;
        private String imageUrl;
        private Integer displayOrder;
    }

}
