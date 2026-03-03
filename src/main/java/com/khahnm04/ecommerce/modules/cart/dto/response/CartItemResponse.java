package com.khahnm04.ecommerce.modules.cart.dto.response;

import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse implements Serializable {

    private Long id;
    private Long productVariantId;
    private String productVariantSku;
    private String productVariantName;
    private String productVariantImage;
    private BigDecimal price;
    private BigDecimal oldPrice;
    private Integer quantity;
    private BigDecimal subtotal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
