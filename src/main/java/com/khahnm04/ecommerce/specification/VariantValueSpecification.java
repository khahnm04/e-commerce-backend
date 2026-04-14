package com.khahnm04.ecommerce.specification;

import com.khahnm04.ecommerce.dto.request.filter.VariantValueFilterRequest;
import com.khahnm04.ecommerce.entity.VariantValue;
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

        Specification<VariantValue> dateSpec = BaseSpecification
                .<VariantValue>withDateRange("createdAt", request.startDate(), request.endDate())
                .or(BaseSpecification.withDateRange("updatedAt", request.startDate(), request.endDate()));

        return BaseSpecification.<VariantValue>withAuditingFetch()
                .and(BaseSpecification.withDeletedStatus(isDeleted))
                .and(BaseSpecification.withKeyword(request.keyword(), KEYWORD_FIELDS))
                .and(BaseSpecification.equals("createdBy.id", request.creatorId()))
                .and(BaseSpecification.equals("variant.id", request.variantId()))
                .and(dateSpec);
    }

}
