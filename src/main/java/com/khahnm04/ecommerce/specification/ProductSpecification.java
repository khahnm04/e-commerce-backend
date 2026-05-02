package com.khahnm04.ecommerce.specification;

import com.khahnm04.ecommerce.dto.request.filter.ProductFilterRequest;
import com.khahnm04.ecommerce.entity.Product;
import com.khahnm04.ecommerce.entity.ProductCategory;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    private static final String[] KEYWORD_FIELDS = {
            "name",
            "slug",
            "code"
    };

    public static Specification<Product> buildSpecification(ProductFilterRequest request, boolean isDeleted) {
        if (request == null) {
            request = ProductFilterRequest.builder().build();
        }

        return withFetch()
                .and(BaseSpecification.withDeletedStatus(isDeleted))
                .and(BaseSpecification.withKeyword(request.keyword(), KEYWORD_FIELDS))
                .and(BaseSpecification.equals("status", request.status()))
                .and(BaseSpecification.equals("brand.id", request.brandId()))
                .and(BaseSpecification.equals("createdBy.id", request.creatorId()))
                .and(BaseSpecification.equals("isFeatured", request.isFeatured()))
                .and(BaseSpecification.equals("isTrending", request.isTrending()))
                .and(withCategory(request.categoryId()))
                .and(BaseSpecification.withDateRange("createdAt", request.startDate(), request.endDate()));
    }

    private static Specification<Product> withFetch() {
        return (root, query, cb) -> {
            if (query != null && query.getResultType().equals(Product.class)) {
                root.fetch("brand", JoinType.LEFT);
                root.fetch("createdBy", JoinType.LEFT);
                root.fetch("updatedBy", JoinType.LEFT);
            }
            return cb.conjunction();
        };
    }

    private static Specification<Product> withCategory(Long categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null || query == null) {
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

}
