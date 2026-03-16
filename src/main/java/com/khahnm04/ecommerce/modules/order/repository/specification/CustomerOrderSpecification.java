package com.khahnm04.ecommerce.modules.order.repository.specification;

import com.khahnm04.ecommerce.common.base.specification.BaseSpecification;
import com.khahnm04.ecommerce.modules.order.dto.request.OrderFilterRequest;
import com.khahnm04.ecommerce.modules.order.entity.Order;
import org.springframework.data.jpa.domain.Specification;

public class CustomerOrderSpecification {

    public static Specification<Order> buildSpecification(Long customerId, OrderFilterRequest request) {
        Specification<Order> spec = BaseSpecification.equals("customer.id", customerId);

        if (request == null) {
            return spec;
        }

        return spec.and(BaseSpecification.equals("status", request.status()))
                .and(BaseSpecification.withDateRange("createdAt", request.startDate(), request.endDate()));
    }

}
