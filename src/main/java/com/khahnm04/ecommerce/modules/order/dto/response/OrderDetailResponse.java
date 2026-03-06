package com.khahnm04.ecommerce.modules.order.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {

    private Long id;
    private Long productVariantId;
    private String productName;
    private String productVariantName;
    private String sku;
    private String image;
    private BigDecimal unitPrice;
    private Long quantity;
    private BigDecimal subtotal;

}
