package com.khahnm04.ecommerce.modules.catalog.dto.request;

import com.khahnm04.ecommerce.modules.catalog.enums.CategoryStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateStatusRequest implements Serializable {

    @NotNull(message = "CATEGORY_STATUS_REQUIRED")
    private CategoryStatus status;

}
