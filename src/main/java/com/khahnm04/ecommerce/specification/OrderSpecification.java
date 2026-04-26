package com.khahnm04.ecommerce.specification;

import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.dto.request.filter.OrderFilterRequest;
import com.khahnm04.ecommerce.entity.Order;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {

    private static final String[] KEYWORD_FIELDS = {
            "code",
            "shippingName",
            "shippingPhone",
            "customer.fullName",
            "customer.email",
            "customer.phoneNumber"
    };

    public static Sort getSort(String sort) {
        if (sort != null) {
            sort = sort.replace("customer", "customer.fullName")
                    .replace("totalAmount", "total");
        }
        return SortUtils.parseSort(sort);
    }

    public static Specification<Order> buildSpecification(OrderFilterRequest request) {
        if (request == null) {
            request = OrderFilterRequest.builder().build();
        }

        return BaseSpecification.<Order>withFetch(JoinType.LEFT, "customer")
                .and(BaseSpecification.withKeyword(request.keyword(), KEYWORD_FIELDS))
                .and(BaseSpecification.withDateRange("createdAt", request.startDate(), request.endDate()))
                .and(BaseSpecification.equals("status", request.status()));
    }

}
