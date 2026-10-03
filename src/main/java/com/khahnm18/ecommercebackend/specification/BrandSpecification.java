package com.khahnm18.ecommercebackend.specification;

import com.khahnm18.ecommercebackend.constant.brand.BrandStatus;
import com.khahnm18.ecommercebackend.entity.brand.Brand;
import org.springframework.data.jpa.domain.Specification;
import java.time.Instant;

public class BrandSpecification {

    private static final String FIELD_NAME = "name";
    private static final String FIELD_SLUG = "slug";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_CREATED_AT = "createdAt";
    private static final String FIELD_IS_DELETED = "isDeleted";

    public static Specification<Brand> byIsDeleted(Boolean isDeleted) {
        return (root, query, cb) ->
            cb.equal(root.get(FIELD_IS_DELETED), isDeleted);
    }

    public static Specification<Brand> byStatus(BrandStatus status) {
        return (root, query, cb) -> {
            if (status == null) return null;
            return cb.equal(root.get(FIELD_STATUS), status);
        };
    }

    public static Specification<Brand> byKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;
            String pattern = "%" + keyword.strip().toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get(FIELD_NAME)), pattern),
                cb.like(cb.lower(root.get(FIELD_SLUG)), pattern)
            );
        };
    }

    public static Specification<Brand> byStartDate(Instant startDate) {
        return (root, query, cb) -> {
            if (startDate == null) return null;
            return cb.greaterThanOrEqualTo(root.get(FIELD_CREATED_AT), startDate);
        };
    }

    public static Specification<Brand> byEndDate(Instant endDate) {
        return (root, query, cb) -> {
            if (endDate == null) return null;
            return cb.lessThanOrEqualTo(root.get(FIELD_CREATED_AT), endDate);
        };
    }

}
