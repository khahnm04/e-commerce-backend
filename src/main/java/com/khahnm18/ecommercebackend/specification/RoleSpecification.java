package com.khahnm18.ecommercebackend.specification;

import com.khahnm18.ecommercebackend.entity.auth.Role;
import org.springframework.data.jpa.domain.Specification;
import java.time.Instant;

public final class RoleSpecification {

    private static final String FIELD_CDOE = "code";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_CREATED_AT = "createdAt";
    private static final String FIELD_IS_DELETED = "isDeleted";

    public static Specification<Role> byIsDeleted(Boolean isDeleted) {
        return (root, query, cb) ->
            cb.equal(root.get(FIELD_IS_DELETED), isDeleted);
    }

    public static Specification<Role> byKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;
            String pattern = "%" + keyword.strip().toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get(FIELD_CDOE)), pattern),
                cb.like(cb.lower(root.get(FIELD_NAME)), pattern)
            );
        };
    }

    public static Specification<Role> byStartDate(Instant startDate) {
        return (root, query, cb) -> {
            if (startDate == null) return null;
            return cb.greaterThanOrEqualTo(root.get(FIELD_CREATED_AT), startDate);
        };
    }

    public static Specification<Role> byEndDate(Instant endDate) {
        return (root, query, cb) -> {
            if (endDate == null) return null;
            return cb.lessThanOrEqualTo(root.get(FIELD_CREATED_AT), endDate);
        };
    }

}
