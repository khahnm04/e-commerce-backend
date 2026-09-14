package com.khahnm18.ecommercebackend.specification;

import com.khahnm18.ecommercebackend.entity.auth.Account;
import org.springframework.data.jpa.domain.Specification;
import java.time.Instant;

public class AdminSpecification {

    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_PHONE_NUMBER = "phoneNumber";
    private static final String FIELD_CREATED_AT = "createdAt";
    private static final String FIELD_IS_DELETED = "isDeleted";

    public static Specification<Account> byIsDeleted(Boolean isDeleted) {
        return (root, query, cb) ->
            cb.equal(root.get(FIELD_IS_DELETED), isDeleted);
    }

    public static Specification<Account> byKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;
            String pattern = "%" + keyword.strip().toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get(FIELD_EMAIL)), pattern),
                cb.like(cb.lower(root.get(FIELD_PHONE_NUMBER)), pattern)
            );
        };
    }

    public static Specification<Account> byStartDate(Instant startDate) {
        return (root, query, cb) -> {
            if (startDate == null) return null;
            return cb.greaterThanOrEqualTo(root.get(FIELD_CREATED_AT), startDate);
        };
    }

    public static Specification<Account> byEndDate(Instant endDate) {
        return (root, query, cb) -> {
            if (endDate == null) return null;
            return cb.lessThanOrEqualTo(root.get(FIELD_CREATED_AT), endDate);
        };
    }

}
