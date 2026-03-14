package com.khahnm04.ecommerce.modules.category.repository.specification;

import com.khahnm04.ecommerce.common.base.specification.BaseSpecification;
import com.khahnm04.ecommerce.modules.category.dto.request.CategoryFilterRequest;
import com.khahnm04.ecommerce.modules.category.entity.Category;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {

    private static final String[] KEYWORD_FIELDS = {
            "name",
            "slug",
            "description"
    };

    public static Specification<Category> buildSpecification(CategoryFilterRequest request, boolean isDeleted) {
        if (request == null) {
            request = CategoryFilterRequest.builder().build();
        }

        return BaseSpecification.<Category>withAuditingFetch()
                .and(BaseSpecification.withDeletedStatus(isDeleted))
                .and(BaseSpecification.withKeyword(request.keyword(), KEYWORD_FIELDS))
                .and(BaseSpecification.withDateRange("createdAt", request.startDate(), request.endDate()))
                .and(BaseSpecification.equals("status", request.status()));
    }

}
