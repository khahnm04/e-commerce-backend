package com.khahnm04.ecommerce.modules.order.repository.specification;

import com.khahnm04.ecommerce.common.base.specification.BaseSpecification;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.order.dto.request.OrderFilterRequest;
import com.khahnm04.ecommerce.modules.order.entity.Order;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {

    private static final String[] KEYWORD_FIELDS = {
            "code",
            "shippingName",
            "shippingPhone",
            "customer.user.fullName",
            "customer.user.email",
            "customer.user.phoneNumber"
    };

    public static Sort getSort(String sort) {
        if (sort != null) {
            sort = sort.replace("customer", "customer.user.fullName")
                    .replace("totalAmount", "total");
        }
        return SortUtils.parseSort(sort);
    }

    public static Specification<Order> buildSpecification(OrderFilterRequest request) {
        if (request == null) {
            request = OrderFilterRequest.builder().build();
        }

        return BaseSpecification.<Order>withFetch(JoinType.LEFT, "customer")
                .and((root, query, cb) -> {
                    if (query != null && query.getResultType().equals(Order.class)) {
                        root.fetch("customer", JoinType.LEFT).fetch("user", JoinType.LEFT);
                    }
                    return cb.conjunction();
                })
                .and(BaseSpecification.withKeyword(request.keyword(), KEYWORD_FIELDS))
                .and(BaseSpecification.withDateRange("createdAt", request.startDate(), request.endDate()))
                .and(BaseSpecification.equals("status", request.status()));
    }

}
