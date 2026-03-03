package com.khahnm04.ecommerce.modules.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeBulkActionRequest implements Serializable {

    @NotEmpty(message = "ATTRIBUTE_SELECTION_REQUIRED")
    private Set<Long> attributes;

    @NotBlank(message = "ATTRIBUTE_ACTION_REQUIRED")
    private String action;

}
