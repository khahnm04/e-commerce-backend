package com.khahnm04.ecommerce.specification.product;

import com.khahnm04.ecommerce.dto.request.product.VariantValueFilterRequest;
import com.khahnm04.ecommerce.entity.product.VariantValue;
import com.khahnm04.ecommerce.specification.BaseSpecification;
import org.springframework.data.jpa.domain.Specification;

public class VariantValueSpecification {

    private static final String[] KEYWORD_FIELDS = {
            "value",
            "variant.name"
    };

    public static Specification<VariantValue> buildSpecification(VariantValueFilterRequest request, boolean isDeleted) {
        if (request == null) {
            request = VariantValueFilterRequest.builder().build();
        }

        return BaseSpecification.<VariantValue>withAuditingFetch()
                .and(BaseSpecification.withDeletedStatus(isDeleted))
                .and(BaseSpecification.withKeyword(request.keyword(), KEYWORD_FIELDS))
                .and(BaseSpecification.equals("variant.id", request.variantId()))
                .and(BaseSpecification.withDateRange("createdAt", request.startDate(), request.endDate()));
    }
}
