package com.khahnm04.ecommerce.modules.cart.dto.response;

import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartSummaryResponse implements Serializable {

    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal shipping;
    private BigDecimal total;
    private String couponCode;
    private Integer totalItems;

}
