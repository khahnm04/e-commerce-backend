package com.khahnm04.ecommerce.modules.auth.repository.specification;

import com.khahnm04.ecommerce.modules.auth.dto.request.RoleFilterRequest;
import com.khahnm04.ecommerce.modules.auth.entity.Role;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

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
        return withFetch()
                .and(withDeletedStatus(isDeleted))
                .and(withKeyword(request.keyword()));
    }

    private static Specification<Role> withFetch() {
        return (root, query, cb) -> {
            if (query == null) {
                return cb.conjunction();
            }
            if (query.getResultType().equals(Role.class)) {
                query.distinct(true);
                if (root.getFetches().isEmpty()) {
                    root.fetch("createdBy", JoinType.LEFT);
                    root.fetch("updatedBy", JoinType.LEFT);
                }
            }
            return cb.conjunction();
        };
    }

    private static Specification<Role> withDeletedStatus(boolean isDeleted) {
        return (root, query, cb) -> isDeleted
                ? cb.isNotNull(root.get("deletedAt"))
                : cb.isNull(root.get("deletedAt"));
    }

    private static Specification<Role> withKeyword(String keywordStr) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(keywordStr)) {
                return cb.conjunction();
            }
            String keyword = "%" + keywordStr.trim().toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();
            for (String field : KEYWORD_FIELDS) {
                predicates.add(cb.like(cb.lower(root.get(field)), keyword));
            }
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

}
