package com.khahnm04.ecommerce.specification.auth;

import com.khahnm04.ecommerce.dto.request.auth.RoleFilterRequest;
import com.khahnm04.ecommerce.entity.auth.Role;
import com.khahnm04.ecommerce.specification.BaseSpecification;
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
                .and(BaseSpecification.equals("createdBy.fullName", request.creator()));
    }

}
