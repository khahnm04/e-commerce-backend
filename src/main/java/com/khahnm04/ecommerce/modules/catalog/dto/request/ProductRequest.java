package com.khahnm04.ecommerce.modules.catalog.dto.request;

import com.khahnm04.ecommerce.modules.catalog.enums.ProductStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest implements Serializable {

    @NotBlank(message = "PRODUCT_NAME_REQUIRED")
    private String name;

    @NotBlank(message = "PRODUCT_SLUG_REQUIRED")
    private String slug;

    private String productCode;

    @NotNull(message = "PRODUCT_BRAND_REQUIRED")
    private Long brandId;

    private Boolean hasSerialNumber;

    private Integer warrantyMonths;

    @NotNull(message = "PRODUCT_PRICE_REQUIRED")
    private BigDecimal price;

    private BigDecimal oldPrice;

    @NotBlank(message = "PRODUCT_THUMBNAIL_REQUIRED")
    private String thumbnail;

    private String description;

    private String shortDescription;

    private String metaTitle;

    private String metaDescription;

    private ProductStatus status;

    private Set<Long> categoryIds;

    @Valid
    private List<ProductAttributeValueRequest> attributes;

    @Valid
    private List<ProductImageRequest> images;

}
