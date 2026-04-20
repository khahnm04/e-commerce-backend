package com.khahnm04.ecommerce.specification;

import com.khahnm04.ecommerce.dto.request.filter.BannerFilterRequest;
import com.khahnm04.ecommerce.entity.Banner;
import org.springframework.data.jpa.domain.Specification;

public class BannerSpecification {

    private static final String[] KEYWORD_FIELDS = {
            "label",
            "heading",
            "description"
    };

    public static Specification<Banner> buildSpecification(BannerFilterRequest request, boolean isDeleted) {
        if (request == null) {
            request = BannerFilterRequest.builder().build();
        }

        Specification<Banner> dateSpec = BaseSpecification
                .<Banner>withDateRange("createdAt", request.startDate(), request.endDate())
                .or(BaseSpecification.withDateRange("updatedAt", request.startDate(), request.endDate()));

        return BaseSpecification.<Banner>withAuditingFetch()
                .and(BaseSpecification.withDeletedStatus(isDeleted))
                .and(BaseSpecification.withKeyword(request.keyword(), KEYWORD_FIELDS))
                .and(BaseSpecification.equals("createdBy.id", request.creatorId()))
                .and(BaseSpecification.equals("position", request.position()))
                .and(BaseSpecification.equals("status", request.status()))
                .and(dateSpec);
    }

}
