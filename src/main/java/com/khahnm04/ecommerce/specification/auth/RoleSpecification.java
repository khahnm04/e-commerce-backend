package com.khahnm04.ecommerce.specification.auth;

import com.khahnm04.ecommerce.dto.filter.RoleFilterRequest;
import com.khahnm04.ecommerce.entity.auth.Role;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class RoleSpecification {

    private static final String FIELD_DELETED_AT = "deletedAt";
    private static final String FIELD_CREATED_AT = "createdAt";
    private static final String[] KEYWORD_FIELDS = {"code", "name", "description"};

    private RoleSpecification() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Specification<Role> buildQuery(RoleFilterRequest request, boolean isDeleted) {
        return byDeletedStatus(isDeleted)
            .and(byKeyword(request.keyword()))
            .and(byStartDate(request.startDate()))
            .and(byEndDate(request.endDate()));
    }

    private static Specification<Role> byDeletedStatus(boolean isDeleted) {
        return (root, query, cb) -> isDeleted
            ? cb.isNotNull(root.get(FIELD_DELETED_AT))
            : cb.isNull(root.get(FIELD_DELETED_AT));
    }

    private static Specification<Role> byKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) return null;
        return (root, query, cb) -> {
            String pattern = "%" + keyword.strip().toLowerCase() + "%";
            List<Predicate> likes = new ArrayList<>(KEYWORD_FIELDS.length);
            for (String field : KEYWORD_FIELDS) {
                likes.add(cb.like(cb.lower(root.get(field)), pattern));
            }
            return cb.or(likes.toArray(Predicate[]::new));
        };
    }

    private static Specification<Role> byStartDate(Instant startDate) {
        if (startDate == null) return null;
        return (root, query, cb) ->
            cb.greaterThanOrEqualTo(root.get(FIELD_CREATED_AT), startDate);
    }

    private static Specification<Role> byEndDate(Instant endDate) {
        if (endDate == null) return null;
        return (root, query, cb) ->
            cb.lessThanOrEqualTo(root.get(FIELD_CREATED_AT), endDate);
    }

}
