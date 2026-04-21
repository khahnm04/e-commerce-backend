package com.khahnm04.ecommerce.specification;

import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.dto.request.filter.EmployeeFilterRequest;
import com.khahnm04.ecommerce.entity.Employee;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {

    private static final String[] KEYWORD_FIELDS = {
            "code",
            "jobTitle",
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

    public static Specification<Employee> buildSpecification(EmployeeFilterRequest request, boolean isDeleted) {
        if (request == null) {
            request = EmployeeFilterRequest.builder().build();
        }

        Specification<Employee> spec = BaseSpecification.<Employee>withFetch(JoinType.INNER, "user")
                .and(BaseSpecification.withAuditingFetch())
                .and(BaseSpecification.withDeletedStatus(isDeleted))
                .and(BaseSpecification.withKeyword(request.keyword(), KEYWORD_FIELDS))
                .and(BaseSpecification.withDateRange("createdAt", request.startDate(), request.endDate()))
                .and(BaseSpecification.equals("user.status", request.status()));

        if (request.roleId() != null) {
            spec = spec.and(withRoleId(request.roleId()));
        }

        return spec;
    }

    private static Specification<Employee> withRoleId(Long roleId) {
        return (root, query, cb) -> {
            var userJoin = root.join("user", JoinType.INNER);
            var rolesJoin = userJoin.join("roles", JoinType.INNER);
            return cb.equal(rolesJoin.get("id"), roleId);
        };
    }

}
