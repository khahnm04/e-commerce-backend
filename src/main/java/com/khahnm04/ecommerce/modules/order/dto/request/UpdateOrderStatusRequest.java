package com.khahnm04.ecommerce.modules.order.dto.request;

import com.khahnm04.ecommerce.modules.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequest {

    @NotNull(message = "ORDER_STATUS_REQUIRED")
    private OrderStatus status;

    private String note;

}
