package com.khahnm04.ecommerce.modules.catalog.dto.response;

import com.khahnm04.ecommerce.common.dto.BaseResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class VariantResponse extends BaseResponse<Long> {

    private String name;
    private String code;

}
