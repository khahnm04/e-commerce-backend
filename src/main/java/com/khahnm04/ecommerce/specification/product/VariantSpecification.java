package com.khahnm04.ecommerce.specification.product;

import com.khahnm04.ecommerce.dto.request.product.VariantFilterRequest;
import com.khahnm04.ecommerce.entity.product.Variant;
import com.khahnm04.ecommerce.specification.BaseSpecification;
import org.springframework.data.jpa.domain.Specification;

public class VariantSpecification {

    private static final String[] KEYWORD_FIELDS = {
            "name"
    };

    public static Specification<Variant> buildSpecification(VariantFilterRequest request, boolean isDeleted) {
        if (request == null) {
            request = VariantFilterRequest.builder().build();
        }

        return BaseSpecification.<Variant>withAuditingFetch()
                .and(BaseSpecification.withDeletedStatus(isDeleted))
                .and(BaseSpecification.withKeyword(request.keyword(), KEYWORD_FIELDS))
                .and(BaseSpecification.withDateRange("createdAt", request.startDate(), request.endDate()));
    }

}
