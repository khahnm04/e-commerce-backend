package com.khahnm04.ecommerce.modules.catalog.repository.specification;

import com.khahnm04.ecommerce.modules.catalog.dto.request.VariantValueFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.entity.VariantValue;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class VariantValueSpecification {

    private static final String[] KEYWORD_FIELDS = {
            "code",
            "value"
    };

    public static Specification<VariantValue> buildSpecification(VariantValueFilterRequest request, boolean isDeleted) {
        if (request == null) {
            request = new VariantValueFilterRequest();
        }
        return withFetch()
                .and(withDeletedStatus(isDeleted))
                .and(withKeyword(request.getKeyword()))
                .and(withVariant(request.getVariantId()))
                .and(withDateRange(request.getStartDate(), request.getEndDate()));
    }

    private static Specification<VariantValue> withFetch() {
        return (root, query, cb) -> {
            if (query == null) {
                return cb.conjunction();
            }
            if (query.getResultType().equals(VariantValue.class)) {
                query.distinct(true);
                if (root.getFetches().isEmpty()) {
                    root.fetch("variant", JoinType.LEFT);
                    root.fetch("createdBy", JoinType.LEFT);
                    root.fetch("updatedBy", JoinType.LEFT);
                }
            }
            return cb.conjunction();
        };
    }

    private static Specification<VariantValue> withDeletedStatus(boolean isDeleted) {
        return (root, query, cb) -> isDeleted
                ? cb.isNotNull(root.get("deletedAt"))
                : cb.isNull(root.get("deletedAt"));
    }

    private static Specification<VariantValue> withKeyword(String keywordStr) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(keywordStr)) {
                return cb.conjunction();
            }
            String keyword = "%" + keywordStr.trim().toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();
            for (String field : KEYWORD_FIELDS) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get(field)),
                                keyword));
            }
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    private static Specification<VariantValue> withVariant(Long variantId) {
        return (root, query, cb) -> {
            if (variantId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("variant").get("id"), variantId);
        };
    }

    private static Specification<VariantValue> withDateRange(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            if (startDate == null && endDate == null) {
                return cb.conjunction();
            }
            if (startDate != null && endDate != null) {
                return cb.between(
                        root.get("createdAt"),
                        startDate.atStartOfDay(),
                        endDate.atTime(LocalTime.MAX));
            }
            if (startDate != null) {
                return cb.greaterThanOrEqualTo(
                        root.get("createdAt"),
                        startDate.atStartOfDay());
            }
            return cb.lessThanOrEqualTo(
                    root.get("createdAt"),
                    endDate.atTime(LocalTime.MAX));
        };
    }
}
