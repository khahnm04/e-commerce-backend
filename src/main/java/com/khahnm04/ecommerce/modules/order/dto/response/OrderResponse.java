package com.khahnm04.ecommerce.modules.order.dto.response;

import com.khahnm04.ecommerce.common.dto.BaseResponse;
import com.khahnm04.ecommerce.modules.order.enums.OrderStatus;
import com.khahnm04.ecommerce.modules.payment.enums.PaymentMethod;
import com.khahnm04.ecommerce.modules.payment.enums.PaymentStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse extends BaseResponse<Long> {

    private String orderCode;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private Long branchId;
    private String branchName;
    private OrderStatus orderStatus;
    private BigDecimal totalAmount;

    private String shippingName;
    private String shippingPhone;
    private String shippingProvince;
    private String shippingDistrict;
    private String shippingWard;
    private String shippingAddressDetail;

    private String note;
    private LocalDate requiredDate;
    private LocalDate shippedDate;

    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;

    private List<OrderDetailResponse> details;

}
