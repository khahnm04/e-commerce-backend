package com.khahnm04.ecommerce.dto.request.order;

import com.khahnm04.ecommerce.common.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.io.Serializable;
import java.util.List;

@Builder
public record OrderCreationRequest(

    @NotEmpty(message = "Order items cannot be empty")
    List<OrderItemRequest> items,

    String couponCode,

    @NotBlank(message = "Shipping name is required")
    String shippingName,

    @NotBlank(message = "Shipping phone is required")
    String shippingPhone,

    @NotBlank(message = "Shipping province is required")
    String shippingProvince,

    @NotBlank(message = "Shipping district is required")
    String shippingDistrict,

    @NotBlank(message = "Shipping ward is required")
    String shippingWard,

    @NotBlank(message = "Shipping address detail is required")
    String shippingAddressDetail,

    String note,

    @NotNull(message = "Payment method is required")
    PaymentMethod paymentMethod

) implements Serializable {
}
