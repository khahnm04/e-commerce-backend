package com.khahnm04.ecommerce.modules.catalog.dto.response;

import com.khahnm04.ecommerce.common.dto.BaseResponse;
import com.khahnm04.ecommerce.modules.catalog.enums.ProductVariantStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantResponse extends BaseResponse<Long> {

    private Long productId;
    private String productName;
    private String sku;
    private String name;
    private BigDecimal price;
    private BigDecimal oldPrice;
    private String image;
    private ProductVariantStatus status;
    private List<VariantValueInfo> variantValues;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VariantValueInfo {
        private Long id;
        private String variantName;
        private String value;
        private String code;
    }

}
