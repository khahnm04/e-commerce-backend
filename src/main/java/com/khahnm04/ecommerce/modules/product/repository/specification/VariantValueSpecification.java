package com.khahnm04.ecommerce.modules.product.repository.specification;

import com.khahnm04.ecommerce.common.base.specification.BaseSpecification;
import com.khahnm04.ecommerce.modules.product.dto.request.VariantValueFilterRequest;
import com.khahnm04.ecommerce.modules.product.entity.VariantValue;
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
