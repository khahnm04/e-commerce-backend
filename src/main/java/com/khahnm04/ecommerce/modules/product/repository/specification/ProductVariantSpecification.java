package com.khahnm04.ecommerce.modules.product.repository.specification;

import com.khahnm04.ecommerce.common.base.specification.BaseSpecification;
import com.khahnm04.ecommerce.modules.product.dto.request.ProductVariantFilterRequest;
import com.khahnm04.ecommerce.modules.product.entity.ProductVariant;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class ProductVariantSpecification {

    private static final String[] KEYWORD_FIELDS = {
            "sku",
            "name"
    };

    public static Specification<ProductVariant> buildSpecification(ProductVariantFilterRequest request,
            boolean isDeleted) {
        if (request == null) {
            request = ProductVariantFilterRequest.builder().build();
        }
        return withFetch()
                .and(BaseSpecification.withDeletedStatus(isDeleted))
                .and(BaseSpecification.withKeyword(request.keyword(), KEYWORD_FIELDS))
                .and(BaseSpecification.equals("product.id", request.productId()))
                .and(BaseSpecification.withDateRange("createdAt", request.startDate(), request.endDate()));
    }

    private static Specification<ProductVariant> withFetch() {
        return (root, query, cb) -> {
            if (query != null && query.getResultType().equals(ProductVariant.class)) {
                root.fetch("product", JoinType.LEFT);
                root.fetch("createdBy", JoinType.LEFT);
                root.fetch("updatedBy", JoinType.LEFT);
            }
            return cb.conjunction();
        };
    }
}
