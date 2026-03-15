package com.khahnm04.ecommerce.modules.catalog.repository.specification;

import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.entity.Product;
import com.khahnm04.ecommerce.modules.catalog.entity.ProductCategory;
import com.khahnm04.ecommerce.modules.catalog.enums.ProductStatus;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    private static final String[] KEYWORD_FIELDS = {
            "name",
            "slug",
            "code"
    };

    public static Specification<Product> buildSpecification(ProductFilterRequest request, boolean isDeleted) {
        if (request == null) {
            request = new ProductFilterRequest();
        }
        return withFetch()
                .and(withDeletedStatus(isDeleted))
                .and(withKeyword(request.getKeyword()))
                .and(withStatus(request.getStatus()))
                .and(withBrand(request.getBrandId()))
                .and(withCategory(request.getCategoryId()))
                .and(withDateRange(request.getStartDate(), request.getEndDate()));
    }

    private static Specification<Product> withFetch() {
        return (root, query, cb) -> {
            if (query == null) {
                return cb.conjunction();
            }
            if (query.getResultType().equals(Product.class)) {
                query.distinct(true);
                if (root.getFetches().isEmpty()) {
                    root.fetch("brand", JoinType.LEFT);
                    root.fetch("createdBy", JoinType.LEFT);
                    root.fetch("updatedBy", JoinType.LEFT);
                }
            }
            return cb.conjunction();
        };
    }

    private static Specification<Product> withDeletedStatus(boolean isDeleted) {
        return (root, query, cb) -> isDeleted
                ? cb.isNotNull(root.get("deletedAt"))
                : cb.isNull(root.get("deletedAt"));
    }

    private static Specification<Product> withKeyword(String keywordStr) {
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

    private static Specification<Product> withStatus(ProductStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }

    private static Specification<Product> withBrand(Long brandId) {
        return (root, query, cb) -> {
            if (brandId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("brand").get("id"), brandId);
        };
    }

    private static Specification<Product> withCategory(Long categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) {
                return cb.conjunction();
            }
            Subquery<Integer> subquery = query.subquery(Integer.class);
            Root<ProductCategory> pcRoot = subquery.from(ProductCategory.class);
            subquery.select(cb.literal(1))
                    .where(
                            cb.equal(pcRoot.get("product").get("id"), root.get("id")),
                            cb.equal(pcRoot.get("category").get("id"), categoryId));
            return cb.exists(subquery);
        };
    }

    private static Specification<Product> withDateRange(LocalDate startDate, LocalDate endDate) {
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
