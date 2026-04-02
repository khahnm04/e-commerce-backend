package com.khahnm04.ecommerce.specification.product;

import com.khahnm04.ecommerce.dto.request.product.AttributeFilterRequest;
import com.khahnm04.ecommerce.entity.product.Attribute;
import com.khahnm04.ecommerce.specification.BaseSpecification;
import org.springframework.data.jpa.domain.Specification;

public class AttributeSpecification {

    private static final String[] KEYWORD_FIELDS = { "name" };

    public static Specification<Attribute> buildSpecification(AttributeFilterRequest request, boolean isDeleted) {
        if (request == null) {
            request = AttributeFilterRequest.builder().build();
        }

        return BaseSpecification.<Attribute>withAuditingFetch()
                .and(BaseSpecification.withDeletedStatus(isDeleted))
                .and(BaseSpecification.withKeyword(request.keyword(), KEYWORD_FIELDS))
                .and(BaseSpecification.withDateRange("createdAt", request.startDate(), request.endDate()));
    }

}
