package com.khahnm04.ecommerce.specification;

import com.khahnm04.ecommerce.dto.request.filter.OrderFilterRequest;
import com.khahnm04.ecommerce.entity.Order;
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
