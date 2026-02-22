package com.khahnm04.ecommerce.modules.user.dto.response;

import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Response cho thống kê khách hàng.
 * Lưu ý: Hiện tại return null để demo, sẽ implement sau khi có Order module.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerStatisticsResponse implements Serializable {

    private Long customerId;
    private Integer totalOrders;
    private BigDecimal totalSpent;
    private Integer loyaltyPoints;
    private String customerLevel;

}
