package com.khahnm04.ecommerce.specification;

import com.khahnm04.ecommerce.dto.request.filter.NewsFilterRequest;
import com.khahnm04.ecommerce.entity.News;
import org.springframework.data.jpa.domain.Specification;

public class NewsSpecification {

    private static final String[] KEYWORD_FIELDS = {
            "title",
            "slug",
            "summary"
    };

    public static Specification<News> buildSpecification(NewsFilterRequest request, boolean isDeleted) {
        if (request == null) {
            request = NewsFilterRequest.builder().build();
        }

        Specification<News> dateSpec = BaseSpecification
                .<News>withDateRange("createdAt", request.startDate(), request.endDate())
                .or(BaseSpecification.withDateRange("updatedAt", request.startDate(), request.endDate()));

        return BaseSpecification.<News>withAuditingFetch()
                .and(BaseSpecification.withDeletedStatus(isDeleted))
                .and(BaseSpecification.withKeyword(request.keyword(), KEYWORD_FIELDS))
                .and(BaseSpecification.equals("createdBy.id", request.creatorId()))
                .and(BaseSpecification.equals("status", request.status()))
                .and(dateSpec);
    }

}
