package com.khahnm18.ecommercebackend.specification;

import com.khahnm18.ecommercebackend.entity.product.ProductGroupCategory;
import com.khahnm18.ecommercebackend.entity.product.ProductGroup;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class ProductGroupSpecification {

    private static final String FIELD_ID = "id";
    private static final String FIELD_GROUP_ID = "groupId";
    private static final String FIELD_CATEGORY_ID = "categoryId";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_SLUG = "slug";
    private static final String FIELD_CREATED_AT = "createdAt";
    private static final String FIELD_IS_DELETED = "isDeleted";

    public static Specification<ProductGroup> byIsDeleted(Boolean isDeleted) {
        return (root, query, cb) ->
            cb.equal(root.get(FIELD_IS_DELETED), isDeleted);
    }

    public static Specification<ProductGroup> byKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;
            String pattern = "%" + keyword.strip().toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get(FIELD_NAME)), pattern),
                cb.like(cb.lower(root.get(FIELD_SLUG)), pattern)
            );
        };
    }

    public static Specification<ProductGroup> byStartDate(Instant startDate) {
        return (root, query, cb) -> {
            if (startDate == null) return null;
            return cb.greaterThanOrEqualTo(root.get(FIELD_CREATED_AT), startDate);
        };
    }

    public static Specification<ProductGroup> byEndDate(Instant endDate) {
        return (root, query, cb) -> {
            if (endDate == null) return null;
            return cb.lessThanOrEqualTo(root.get(FIELD_CREATED_AT), endDate);
        };
    }

    public static Specification<ProductGroup> byCategoryId(UUID categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) return null;
            Subquery<Integer> sub = Objects.requireNonNull(query).subquery(Integer.class);
            Root<ProductGroupCategory> productCategory = sub.from(ProductGroupCategory.class);
            sub.select(cb.literal(1))
                .where(
                    cb.equal(productCategory.get(FIELD_GROUP_ID), root.get(FIELD_ID)),
                    cb.equal(productCategory.get(FIELD_CATEGORY_ID), categoryId)
                );
            return cb.exists(sub);
        };
    }

}
