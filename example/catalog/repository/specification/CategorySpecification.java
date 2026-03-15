package com.khahnm04.ecommerce.modules.catalog.repository.specification;

import com.khahnm04.ecommerce.modules.catalog.dto.request.CategoryFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.entity.Category;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CategorySpecification {

    private static final String[] KEYWORD_FIELDS = {
            "name",
            "slug",
            "description"
    };

    public static Specification<Category> buildSpecification(CategoryFilterRequest request, boolean isDeleted) {
        if (request == null) {
            request = new CategoryFilterRequest();
        }
        return withFetch()
                .and(withDeletedStatus(isDeleted))
                .and(withKeyword(request.getKeyword()))
                .and(withParent(request.getParentId()))
                .and(withDateRange(request.getStartDate(), request.getEndDate()));
    }

    private static Specification<Category> withFetch() {
        return (root, query, cb) -> {
            if (query == null) {
                return cb.conjunction();
            }
            if (query.getResultType().equals(Category.class)) {
                query.distinct(true);
                if (root.getFetches().isEmpty()) {
                    root.fetch("parent", JoinType.LEFT);
                    root.fetch("createdBy", JoinType.LEFT);
                    root.fetch("updatedBy", JoinType.LEFT);
                }
            }
            return cb.conjunction();
        };
    }

    private static Specification<Category> withDeletedStatus(boolean isDeleted) {
        return (root, query, cb) -> isDeleted
                ? cb.isNotNull(root.get("deletedAt"))
                : cb.isNull(root.get("deletedAt"));
    }

    private static Specification<Category> withKeyword(String keywordStr) {
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

    private static Specification<Category> withParent(Long parentId) {
        return (root, query, cb) -> {
            if (parentId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("parent").get("id"), parentId);
        };
    }

    private static Specification<Category> withDateRange(LocalDate startDate, LocalDate endDate) {
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
