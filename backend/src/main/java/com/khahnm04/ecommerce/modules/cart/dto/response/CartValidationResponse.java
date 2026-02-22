package com.khahnm04.ecommerce.modules.cart.dto.response;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartValidationResponse implements Serializable {

    private boolean valid;
    private Long productVariantId;
    private String productVariantName;
    private String message;

}
