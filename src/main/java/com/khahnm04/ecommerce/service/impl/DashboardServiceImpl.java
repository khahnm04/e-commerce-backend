package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.common.enums.OrderStatus;
import com.khahnm04.ecommerce.dto.response.DashboardSummaryResponse;
import com.khahnm04.ecommerce.repository.CustomerRepository;
import com.khahnm04.ecommerce.repository.OrderRepository;
import com.khahnm04.ecommerce.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    @Override
    public DashboardSummaryResponse getSummary() {
        return DashboardSummaryResponse.builder()
                .totalCustomers(customerRepository.countByDeletedAtIsNull())
                .totalOrders(orderRepository.countByDeletedAtIsNull())
                .totalRevenue(orderRepository.sumTotalRevenue(OrderStatus.COMPLETED))
                .build();
    }

}
