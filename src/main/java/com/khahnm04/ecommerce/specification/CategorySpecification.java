package com.khahnm04.ecommerce.specification;

import com.khahnm04.ecommerce.dto.request.filter.CategoryFilterRequest;
import com.khahnm04.ecommerce.entity.Category;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {

    private static final String[] KEYWORD_FIELDS = {
            "name",
            "slug"
    };

    public static Specification<Category> buildSpecification(CategoryFilterRequest request, boolean isDeleted) {
        if (request == null) {
            request = CategoryFilterRequest.builder().build();
        }

        Specification<Category> dateSpec = BaseSpecification
                .<Category>withDateRange("createdAt", request.startDate(), request.endDate())
                .or(BaseSpecification.withDateRange("updatedAt", request.startDate(), request.endDate()));

        Specification<Category> spec = BaseSpecification.<Category>withAuditingFetch()
                .and(BaseSpecification.withDeletedStatus(isDeleted))
                .and(BaseSpecification.withKeyword(request.keyword(), KEYWORD_FIELDS))
                .and(BaseSpecification.equals("createdBy.id", request.creatorId()))
                .and(BaseSpecification.equals("status", request.status()))
                .and(BaseSpecification.equals("parent.id", request.parentId()))
                .and(BaseSpecification.equals("isFeatured", request.isFeatured()))
                .and(BaseSpecification.equals("showInMenu", request.showInMenu()))
                .and(dateSpec);

        if (Boolean.TRUE.equals(request.isRoot())) {
            spec = spec.and((root, query, cb) -> cb.isNull(root.get("parent")));
        }

        return spec;
    }

}
