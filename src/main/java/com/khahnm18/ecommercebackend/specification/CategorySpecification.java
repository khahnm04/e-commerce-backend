package com.khahnm18.ecommercebackend.specification;

import com.khahnm18.ecommercebackend.constant.category.CategoryStatus;
import com.khahnm18.ecommercebackend.entity.category.Category;
import org.springframework.data.jpa.domain.Specification;
import java.time.Instant;
import java.util.UUID;

public class CategorySpecification {

    private static final String FIELD_NAME = "name";
    private static final String FIELD_SLUG = "slug";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_PARENT_ID = "parentId";
    private static final String FIELD_CREATED_AT = "createdAt";
    private static final String FIELD_IS_DELETED = "isDeleted";

    public static Specification<Category> byIsDeleted(Boolean isDeleted) {
        return (root, query, cb) ->
            cb.equal(root.get(FIELD_IS_DELETED), isDeleted);
    }

    public static Specification<Category> byStatus(CategoryStatus status) {
        return (root, query, cb) -> {
            if (status == null) return null;
            return cb.equal(root.get(FIELD_STATUS), status);
        };
    }

    public static Specification<Category> byParentId(UUID parentId) {
        return (root, query, cb) -> {
            if (parentId == null) return null;
            return cb.equal(root.get(FIELD_PARENT_ID), parentId);
        };
    }

    public static Specification<Category> byKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;
            String pattern = "%" + keyword.strip().toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get(FIELD_NAME)), pattern),
                cb.like(cb.lower(root.get(FIELD_SLUG)), pattern)
            );
        };
    }

    public static Specification<Category> byStartDate(Instant startDate) {
        return (root, query, cb) -> {
            if (startDate == null) return null;
            return cb.greaterThanOrEqualTo(root.get(FIELD_CREATED_AT), startDate);
        };
    }

    public static Specification<Category> byEndDate(Instant endDate) {
        return (root, query, cb) -> {
            if (endDate == null) return null;
            return cb.lessThanOrEqualTo(root.get(FIELD_CREATED_AT), endDate);
        };
    }

}
