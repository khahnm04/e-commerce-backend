package com.khahnm04.ecommerce.modules.cart.dto.response;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse implements Serializable {

    private Long id;
    private String couponCode;
    private List<CartItemResponse> items;
    private CartSummaryResponse summary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
