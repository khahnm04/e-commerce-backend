package com.khahnm04.ecommerce.dto.response.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.common.enums.OrderStatus;
import com.khahnm04.ecommerce.common.enums.PaymentMethod;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderResponse(

    Long id,
    String code,
    BigDecimal subTotal,
    BigDecimal discount,
    BigDecimal total,
    OrderStatus status,
    PaymentMethod paymentMethod,
    // Shipping info
    String shippingName,
    String shippingPhone,
    String shippingProvince,
    String shippingDistrict,
    String shippingWard,
    String shippingAddressDetail,
    String note,
    String customer,
    Integer totalQuantity,
    // Items detail
    List<OrderItemResponse> items,
    String paymentUrl,
    LocalDateTime estimatedDeliveryDate,
    LocalDateTime createdAt

) implements Serializable {
}
