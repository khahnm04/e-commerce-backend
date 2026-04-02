package com.khahnm04.ecommerce.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BaseSpecification {

    public static <T> Specification<T> withFetch(String... associations) {
        return withFetch(JoinType.LEFT, associations);
    }

    public static <T> Specification<T> withFetch(JoinType joinType, String... associations) {
        return (root, query, cb) -> {
            if (query == null || !query.getResultType().equals(root.getJavaType())) {
                return cb.conjunction();
            }
            for (String association : associations) {
                boolean alreadyFetched = root.getFetches().stream()
                        .anyMatch(f -> f.getAttribute().getName().equals(association));
                if (!alreadyFetched) {
                    root.fetch(association, joinType);
                }
            }
            return cb.conjunction();
        };
    }

    public static <T> Specification<T> withAuditingFetch() {
        return withFetch("createdBy", "updatedBy");
    }

    public static <T> Specification<T> withDeletedStatus(boolean isDeleted) {
        return (root, query, cb) -> isDeleted
                ? cb.isNotNull(root.get("deletedAt"))
                : cb.isNull(root.get("deletedAt"));
    }

    public static <T> Specification<T> withKeyword(String keywordStr, String... fields) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(keywordStr) || fields.length == 0) {
                return cb.conjunction();
            }
            String keyword = "%" + keywordStr.trim().toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();
            for (String field : fields) {
                predicates.add(cb.like(cb.lower(getExpression(root, field)), keyword));
            }
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static <T> Specification<T> withDateRange(String field, LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            if (startDate == null && endDate == null) {
                return cb.conjunction();
            }
            if (startDate != null && endDate != null) {
                return cb.between(root.get(field), startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
            }
            if (startDate != null) {
                return cb.greaterThanOrEqualTo(root.get(field), startDate.atStartOfDay());
            }
            return cb.lessThanOrEqualTo(root.get(field), endDate.atTime(LocalTime.MAX));
        };
    }

    public static <T, V> Specification<T> equals(String field, V value) {
        return (root, query, cb) -> value == null
                ? cb.conjunction()
                : cb.equal(getPath(root, field), value);
    }

    private static <T> Path<?> getPath(Root<T> root, String fieldPath) {
        if (!fieldPath.contains(".")) {
            return root.get(fieldPath);
        }
        String[] parts = fieldPath.split("\\.");
        Path<?> path = root.get(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            path = path.get(parts[i]);
        }
        return path;
    }

    private static <T> Expression<String> getExpression(Root<T> root, String fieldPath) {
        return getPath(root, fieldPath).as(String.class);
    }

}
