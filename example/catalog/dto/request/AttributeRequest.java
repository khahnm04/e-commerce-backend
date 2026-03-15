package com.khahnm04.ecommerce.modules.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeRequest implements Serializable {

    @NotBlank(message = "ATTRIBUTE_NAME_REQUIRED")
    private String name;

    @NotBlank(message = "ATTRIBUTE_CODE_REQUIRED")
    private String code;

    private String description;

}
