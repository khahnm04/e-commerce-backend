package com.khahnm04.ecommerce.specification;

import com.khahnm04.ecommerce.dto.request.filter.CustomerFilterRequest;
import com.khahnm04.ecommerce.entity.Customer;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecification {

    private static final String[] KEYWORD_FIELDS = {
            "fullName",
            "email",
            "phoneNumber"
    };

    public static Specification<Customer> buildSpecification(CustomerFilterRequest request, boolean isDeleted) {
        if (request == null) {
            request = CustomerFilterRequest.builder().build();
        }

        Specification<Customer> dateSpec = BaseSpecification
                .<Customer>withDateRange("createdAt", request.startDate(), request.endDate())
                .or(BaseSpecification.withDateRange("updatedAt", request.startDate(), request.endDate()));

        return BaseSpecification.<Customer>withAuditingFetch()
                .and(BaseSpecification.withDeletedStatus(isDeleted))
                .and(BaseSpecification.withKeyword(request.keyword(), KEYWORD_FIELDS))
                .and(BaseSpecification.equals("createdBy.id", request.creatorId()))
                .and(BaseSpecification.equals("status", request.status()))
                .and(BaseSpecification.equals("gender", request.gender()))
                .and(dateSpec);
    }

}
