package com.khahnm04.ecommerce.modules.brand.repository.specification;

import com.khahnm04.ecommerce.common.base.specification.BaseSpecification;
import com.khahnm04.ecommerce.modules.brand.dto.request.BrandFilterRequest;
import com.khahnm04.ecommerce.modules.brand.entity.Brand;
import org.springframework.data.jpa.domain.Specification;

public class BrandSpecification {

    private static final String[] KEYWORD_FIELDS = {
            "name",
            "slug",
            "description",
            "country"
    };

    public static Specification<Brand> buildSpecification(BrandFilterRequest request, boolean isDeleted) {
        if (request == null) {
            request = BrandFilterRequest.builder().build();
        }

        return BaseSpecification.<Brand>withAuditingFetch()
                .and(BaseSpecification.withDeletedStatus(isDeleted))
                .and(BaseSpecification.withKeyword(request.keyword(), KEYWORD_FIELDS))
                .and(BaseSpecification.withDateRange("createdAt", request.startDate(), request.endDate()));
    }

}
