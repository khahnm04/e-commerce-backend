package com.khahnm04.ecommerce.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {

    private long totalCustomers;
    private long totalOrders;
    private BigDecimal totalRevenue;

}
