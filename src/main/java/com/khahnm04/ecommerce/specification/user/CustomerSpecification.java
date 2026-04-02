package com.khahnm04.ecommerce.specification.user;

import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.dto.request.user.CustomerFilterRequest;
import com.khahnm04.ecommerce.entity.user.Customer;
import com.khahnm04.ecommerce.specification.BaseSpecification;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecification {

    private static final String[] KEYWORD_FIELDS = {
            "user.fullName",
            "user.email",
            "user.phoneNumber"
    };

    public static Sort getSort(String sort) {
        if (sort != null) {
            sort = sort.replace("fullName", "user.fullName")
                    .replace("email", "user.email")
                    .replace("phoneNumber", "user.phoneNumber")
                    .replace("status", "user.status");
        }
        return SortUtils.parseSort(sort);
    }

    public static Specification<Customer> buildSpecification(CustomerFilterRequest request, boolean isDeleted) {
        if (request == null) {
            request = CustomerFilterRequest.builder().build();
        }

        return BaseSpecification.<Customer>withFetch(JoinType.INNER, "user")
                .and(BaseSpecification.withAuditingFetch())
                .and(BaseSpecification.withDeletedStatus(isDeleted))
                .and(BaseSpecification.withKeyword(request.keyword(), KEYWORD_FIELDS))
                .and(BaseSpecification.withDateRange("createdAt", request.startDate(), request.endDate()))
                .and(BaseSpecification.equals("user.status", request.status()))
                .and(BaseSpecification.equals("user.gender", request.gender()));
    }
}
