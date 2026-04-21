package com.khahnm04.ecommerce.specification;

import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.dto.request.filter.CustomerFilterRequest;
import com.khahnm04.ecommerce.entity.Customer;
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
        if (sort == null || sort.trim().isEmpty()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }

        String actualSort = sort.replace("fullName", "user.fullName")
                .replace("email", "user.email")
                .replace("phoneNumber", "user.phoneNumber")
                .replace("status", "user.status")
                .replace("lastLoginAt", "user.lastLoginAt");

        return SortUtils.parseSort(actualSort);
    }

    public static Specification<Customer> buildSpecification(CustomerFilterRequest request, boolean isDeleted) {
        if (request == null) {
            request = CustomerFilterRequest.builder().build();
        }

        Specification<Customer> dateSpec = BaseSpecification
                .<Customer>withDateRange("createdAt", request.startDate(), request.endDate())
                .or(BaseSpecification.withDateRange("updatedAt", request.startDate(), request.endDate()));

        return BaseSpecification.<Customer>withFetch(JoinType.INNER, "user")
                .and(BaseSpecification.withAuditingFetch())
                .and(BaseSpecification.withDeletedStatus(isDeleted))
                .and(BaseSpecification.withKeyword(request.keyword(), KEYWORD_FIELDS))
                .and(BaseSpecification.equals("createdBy.id", request.creatorId()))
                .and(BaseSpecification.equals("user.status", request.status()))
                .and(BaseSpecification.equals("user.gender", request.gender()))
                .and(dateSpec);
    }

}
