package com.khahnm04.ecommerce.specification;

import com.khahnm04.ecommerce.constant.category.CategoryStatus;
import com.khahnm04.ecommerce.dto.request.category.CategoryFilterRequest;
import com.khahnm04.ecommerce.entity.category.Category;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class CategorySpecification {

    private static final String FIELD_IS_DELETED = "isDeleted";
    private static final String FIELD_CREATED_AT = "createdAt";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_IS_FEATURED = "isFeatured";
    private static final String FIELD_SHOW_IN_MENU = "showInMenu";
    private static final String FIELD_PARENT_ID = "parentId";
    private static final String[] KEYWORD_FIELDS = {"name", "slug"};

    private CategorySpecification() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Specification<Category> buildQuery(CategoryFilterRequest request, boolean isDeleted) {
        return byIsDeleted(isDeleted)
            .and(byKeyword(request.keyword()))
            .and(byStartDate(request.startDate()))
            .and(byEndDate(request.endDate()))
            .and(byStatus(request.status()))
            .and(byIsFeatured(request.isFeatured()))
            .and(byShowInMenu(request.showInMenu()))
            .and(byParentId(request.parentId()));
    }

    private static Specification<Category> byIsDeleted(boolean isDeleted) {
        return (root, query, cb) ->
            cb.equal(root.get(FIELD_IS_DELETED), isDeleted);
    }

    private static Specification<Category> byKeyword(String keyword) {
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

    private static Specification<Category> byStartDate(Instant startDate) {
        if (startDate == null) return null;
        return (root, query, cb) ->
            cb.greaterThanOrEqualTo(root.get(FIELD_CREATED_AT), startDate);
    }

    private static Specification<Category> byEndDate(Instant endDate) {
        if (endDate == null) return null;
        return (root, query, cb) ->
            cb.lessThanOrEqualTo(root.get(FIELD_CREATED_AT), endDate);
    }

    private static Specification<Category> byStatus(CategoryStatus status) {
        if (status == null) return null;
        return (root, query, cb) ->
            cb.equal(root.get(FIELD_STATUS), status);
    }

    private static Specification<Category> byParentId(Long parentId) {
        if (parentId == null)  return null;
        return (root, query, cb) ->
            cb.equal(root.get(FIELD_PARENT_ID), parentId);
    }

    private static Specification<Category> byIsFeatured(Boolean isFeatured) {
        if (isFeatured == null) return null;
        return (root, query, cb) ->
            cb.equal(root.get(FIELD_IS_FEATURED), isFeatured);
    }

    private static Specification<Category> byShowInMenu(Boolean showInMenu) {
        if (showInMenu == null) return null;
        return (root, query, cb) ->
            cb.equal(root.get(FIELD_SHOW_IN_MENU), showInMenu);
    }

}
