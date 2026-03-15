package com.khahnm04.ecommerce.modules.catalog.dto.response;

import com.khahnm04.ecommerce.common.dto.BaseResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class VariantValueResponse extends BaseResponse<Long> {

    private Long variantId;
    private String variantName;
    private String code;
    private String value;
    private Integer displayOrder;

}
