package com.khahnm04.ecommerce.modules.cart.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyCouponRequest implements Serializable {

    @NotBlank(message = "CART_COUPON_CODE_REQUIRED")
    private String couponCode;

}
