package com.khahnm04.ecommerce.specification;

import com.khahnm04.ecommerce.dto.request.filter.RoleFilterRequest;
import com.khahnm04.ecommerce.entity.Role;
import org.springframework.data.jpa.domain.Specification;

public class RoleSpecification {

    private static final String[] KEYWORD_FIELDS = {
            "code",
            "name",
            "description"
    };

    public static Specification<Role> buildSpecification(RoleFilterRequest request, boolean isDeleted) {
        if (request == null) {
            request = RoleFilterRequest.builder().build();
        }
        return BaseSpecification.<Role>withAuditingFetch()
                .and(BaseSpecification.withDeletedStatus(isDeleted))
                .and(BaseSpecification.withKeyword(request.keyword(), KEYWORD_FIELDS))
                .and(BaseSpecification.withDateRange("createdAt", request.startDate(), request.endDate()))
                .and(BaseSpecification.equals("createdBy.id", request.creatorId()));
    }

}
