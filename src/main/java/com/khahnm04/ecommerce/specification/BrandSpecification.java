package com.khahnm04.ecommerce.specification;

import com.khahnm04.ecommerce.constant.brand.BrandStatus;
import com.khahnm04.ecommerce.dto.request.brand.BrandFilterRequest;
import com.khahnm04.ecommerce.entity.brand.Brand;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class BrandSpecification {

    private static final String FIELD_IS_DELETED = "isDeleted";
    private static final String FIELD_CREATED_AT = "createdAt";
    private static final String FIELD_STATUS = "status";
    private static final String[] KEYWORD_FIELDS = {"name", "slug"};

    private BrandSpecification() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Specification<Brand> buildQuery(BrandFilterRequest request, boolean isDeleted) {
        return byIsDeleted(isDeleted)
            .and(byKeyword(request.keyword()))
            .and(byStartDate(request.startDate()))
            .and(byEndDate(request.endDate()))
            .and(byStatus(request.status()));
    }

    private static Specification<Brand> byIsDeleted(boolean isDeleted) {
        return (root, query, cb) ->
            cb.equal(root.get(FIELD_IS_DELETED), isDeleted);
    }

    private static Specification<Brand> byKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) return null;
        return (root, query, cb) -> {
            String pattern = "%" + keyword.strip().toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();
            for (String field : KEYWORD_FIELDS) {
                predicates.add(cb.like(cb.lower(root.get(field)), pattern));
            }
            return cb.or(predicates.toArray(Predicate[]::new));
        };
    }

    private static Specification<Brand> byStartDate(Instant startDate) {
        if (startDate == null) return null;
        return (root, query, cb) ->
            cb.greaterThanOrEqualTo(root.get(FIELD_CREATED_AT), startDate);
    }

    private static Specification<Brand> byEndDate(Instant endDate) {
        if (endDate == null) return null;
        return (root, query, cb) ->
            cb.lessThanOrEqualTo(root.get(FIELD_CREATED_AT), endDate);
    }

    private static Specification<Brand> byStatus(BrandStatus status) {
        if (status == null) return null;
        return (root, query, cb) ->
            cb.equal(root.get(FIELD_STATUS), status);
    }

}
