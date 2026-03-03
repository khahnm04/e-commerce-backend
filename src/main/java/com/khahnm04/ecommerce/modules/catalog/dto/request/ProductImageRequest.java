package com.khahnm04.ecommerce.modules.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageRequest implements Serializable {

    private Long id;

    @NotBlank(message = "PRODUCT_IMAGE_URL_REQUIRED")
    private String imageUrl;

    private Integer displayOrder;

}
