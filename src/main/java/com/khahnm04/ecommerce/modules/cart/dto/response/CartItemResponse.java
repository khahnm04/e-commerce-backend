package com.khahnm04.ecommerce.modules.cart.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {

    private Long id;
    private Long productVariantId;
    private String productName;
    private String productVariantName;
    private String sku;
    private String image;
    private BigDecimal price;
    private BigDecimal oldPrice;
    private Integer quantity;
    private BigDecimal subtotal;

}
