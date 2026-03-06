package com.khahnm04.ecommerce.modules.order.dto.request;

import com.khahnm04.ecommerce.modules.payment.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    @NotEmpty(message = "ORDER_CART_ITEM_IDS_REQUIRED")
    private Set<Long> cartItemIds;

    @NotBlank(message = "ORDER_SHIPPING_NAME_REQUIRED")
    private String shippingName;

    @NotBlank(message = "ORDER_SHIPPING_PHONE_REQUIRED")
    private String shippingPhone;

    @NotBlank(message = "ORDER_SHIPPING_PROVINCE_REQUIRED")
    private String shippingProvince;

    @NotBlank(message = "ORDER_SHIPPING_DISTRICT_REQUIRED")
    private String shippingDistrict;

    @NotBlank(message = "ORDER_SHIPPING_WARD_REQUIRED")
    private String shippingWard;

    @NotBlank(message = "ORDER_SHIPPING_ADDRESS_DETAIL_REQUIRED")
    private String shippingAddressDetail;

    private String note;

    @NotNull(message = "ORDER_PAYMENT_METHOD_REQUIRED")
    private PaymentMethod paymentMethod;

}
