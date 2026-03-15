package com.khahnm04.ecommerce.modules.catalog.repository.specification;

import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductVariantFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.entity.ProductVariant;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ProductVariantSpecification {

    private static final String[] KEYWORD_FIELDS = {
            "sku",
            "name"
    };

    public static Specification<ProductVariant> buildSpecification(ProductVariantFilterRequest request,
            boolean isDeleted) {
        if (request == null) {
            request = new ProductVariantFilterRequest();
        }
        return withFetch()
                .and(withDeletedStatus(isDeleted))
                .and(withKeyword(request.getKeyword()))
                .and(withProduct(request.getProductId()))
                .and(withDateRange(request.getStartDate(), request.getEndDate()));
    }

    private static Specification<ProductVariant> withFetch() {
        return (root, query, cb) -> {
            if (query == null) {
                return cb.conjunction();
            }
            if (query.getResultType().equals(ProductVariant.class)) {
                query.distinct(true);
                if (root.getFetches().isEmpty()) {
                    root.fetch("product", JoinType.LEFT);
                    root.fetch("createdBy", JoinType.LEFT);
                    root.fetch("updatedBy", JoinType.LEFT);
                }
            }
            return cb.conjunction();
        };
    }

    private static Specification<ProductVariant> withDeletedStatus(boolean isDeleted) {
        return (root, query, cb) -> isDeleted
                ? cb.isNotNull(root.get("deletedAt"))
                : cb.isNull(root.get("deletedAt"));
    }

    private static Specification<ProductVariant> withKeyword(String keywordStr) {
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

    private static Specification<ProductVariant> withProduct(Long productId) {
        return (root, query, cb) -> {
            if (productId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("product").get("id"), productId);
        };
    }

    private static Specification<ProductVariant> withDateRange(LocalDate startDate, LocalDate endDate) {
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
