package com.khahnm04.ecommerce.modules.product.repository.specification;

import com.khahnm04.ecommerce.common.base.specification.BaseSpecification;
import com.khahnm04.ecommerce.modules.product.dto.request.AttributeFilterRequest;
import com.khahnm04.ecommerce.modules.product.entity.Attribute;
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
