package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DashboardSummaryResponse (

    long totalCustomers,
    long totalOrders,
    BigDecimal totalRevenue

) implements Serializable {
}

