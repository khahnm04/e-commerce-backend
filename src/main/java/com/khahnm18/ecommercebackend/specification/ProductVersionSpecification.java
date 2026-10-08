package com.khahnm18.ecommercebackend.specification;

import com.khahnm18.ecommercebackend.entity.product.ProductVersion;
import org.springframework.data.jpa.domain.Specification;
import java.time.Instant;
import java.util.UUID;

public class ProductVersionSpecification {

    private static final String FIELD_NAME = "name";
    private static final String FIELD_SLUG = "slug";
    private static final String FIELD_VERSION = "version";
    private static final String FIELD_GROUP_ID = "groupId";
    private static final String FIELD_CREATED_AT = "createdAt";
    private static final String FIELD_IS_DELETED = "isDeleted";

    public static Specification<ProductVersion> byIsDeleted(Boolean isDeleted) {
        return (root, query, cb) ->
            cb.equal(root.get(FIELD_IS_DELETED), isDeleted);
    }

    public static Specification<ProductVersion> byKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;
            String pattern = "%" + keyword.strip().toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get(FIELD_NAME)), pattern),
                cb.like(cb.lower(root.get(FIELD_SLUG)), pattern),
                cb.like(cb.lower(root.get(FIELD_VERSION)), pattern)
            );
        };
    }

    public static Specification<ProductVersion> byStartDate(Instant startDate) {
        return (root, query, cb) -> {
            if (startDate == null) return null;
            return cb.greaterThanOrEqualTo(root.get(FIELD_CREATED_AT), startDate);
        };
    }

    public static Specification<ProductVersion> byEndDate(Instant endDate) {
        return (root, query, cb) -> {
            if (endDate == null) return null;
            return cb.lessThanOrEqualTo(root.get(FIELD_CREATED_AT), endDate);
        };
    }

    public static Specification<ProductVersion> byGroupId(UUID groupId) {
        return (root, query, cb) -> {
            if (groupId == null) return null;
            return cb.equal(root.get(FIELD_GROUP_ID), groupId);
        };
    }

}
