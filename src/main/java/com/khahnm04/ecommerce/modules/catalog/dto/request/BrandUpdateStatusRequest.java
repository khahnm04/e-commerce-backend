package com.khahnm04.ecommerce.modules.catalog.dto.request;

import com.khahnm04.ecommerce.modules.catalog.enums.BrandStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandUpdateStatusRequest implements Serializable {

    @NotNull(message = "BRAND_STATUS_REQUIRED")
    private BrandStatus status;

}
