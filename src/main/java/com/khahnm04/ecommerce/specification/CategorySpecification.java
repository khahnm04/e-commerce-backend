package com.khahnm04.ecommerce.specification;

import com.khahnm04.ecommerce.dto.request.filter.CategoryFilterRequest;
import com.khahnm04.ecommerce.entity.Category;
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
