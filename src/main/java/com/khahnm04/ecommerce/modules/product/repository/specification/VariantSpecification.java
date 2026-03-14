package com.khahnm04.ecommerce.modules.product.repository.specification;

import com.khahnm04.ecommerce.common.base.specification.BaseSpecification;
import com.khahnm04.ecommerce.modules.product.dto.request.VariantFilterRequest;
import com.khahnm04.ecommerce.modules.product.entity.Variant;
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
