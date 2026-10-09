package com.khahnm18.ecommercebackend.specification;

import com.khahnm18.ecommercebackend.entity.product.Product;
import com.khahnm18.ecommercebackend.enums.ProductStatus;
import org.springframework.data.jpa.domain.Specification;
import java.time.Instant;
import java.util.UUID;

public class ProductSpecification {

    private static final String FIELD_NAME = "name";
    private static final String FIELD_SLUG = "slug";
    private static final String FIELD_COLOR = "color";
    private static final String FIELD_SKU = "sku";
    private static final String FIELD_VERSION_ID = "versionId";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_CREATED_AT = "createdAt";
    private static final String FIELD_IS_DELETED = "isDeleted";

    public static Specification<Product> byIsDeleted(Boolean isDeleted) {
        return (root, query, cb) ->
            cb.equal(root.get(FIELD_IS_DELETED), isDeleted);
    }

    public static Specification<Product> byStatus(ProductStatus status) {
        return (root, query, cb) -> {
            if (status == null) return null;
            return cb.equal(root.get(FIELD_STATUS), status);
        };
    }

    public static Specification<Product> byKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;
            String pattern = "%" + keyword.strip().toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get(FIELD_NAME)), pattern),
                cb.like(cb.lower(root.get(FIELD_SLUG)), pattern),
                cb.like(cb.lower(root.get(FIELD_COLOR)), pattern),
                cb.like(cb.lower(root.get(FIELD_SKU)), pattern)
            );
        };
    }

    public static Specification<Product> byStartDate(Instant startDate) {
        return (root, query, cb) -> {
            if (startDate == null) return null;
            return cb.greaterThanOrEqualTo(root.get(FIELD_CREATED_AT), startDate);
        };
    }

    public static Specification<Product> byEndDate(Instant endDate) {
        return (root, query, cb) -> {
            if (endDate == null) return null;
            return cb.lessThanOrEqualTo(root.get(FIELD_CREATED_AT), endDate);
        };
    }

    public static Specification<Product> byVersionId(UUID versionId) {
        return (root, query, cb) -> {
            if (versionId == null) return null;
            return cb.equal(root.get(FIELD_VERSION_ID), versionId);
        };
    }

}
