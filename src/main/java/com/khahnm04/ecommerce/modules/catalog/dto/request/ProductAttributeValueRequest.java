package com.khahnm04.ecommerce.modules.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeValueRequest implements Serializable {

    private Long id;

    @NotNull(message = "PRODUCT_ATTRIBUTE_ID_REQUIRED")
    private Long attributeId;

    @NotBlank(message = "PRODUCT_ATTRIBUTE_VALUE_REQUIRED")
    private String value;

}
