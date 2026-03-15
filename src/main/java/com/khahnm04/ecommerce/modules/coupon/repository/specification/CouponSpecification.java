package com.khahnm04.ecommerce.modules.coupon.repository.specification;

import com.khahnm04.ecommerce.common.base.specification.BaseSpecification;
import com.khahnm04.ecommerce.modules.coupon.dto.request.CouponFilterRequest;
import com.khahnm04.ecommerce.modules.coupon.entity.Coupon;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class CouponSpecification {

    private static final String[] KEYWORD_FIELDS = {
            "code",
            "name"
    };

    public static Specification<Coupon> buildSpecification(CouponFilterRequest request, boolean isDeleted) {
        if (request == null) {
            request = CouponFilterRequest.builder().build();
        }
        return withFetch()
                .and(BaseSpecification.withDeletedStatus(isDeleted))
                .and(BaseSpecification.withKeyword(request.keyword(), KEYWORD_FIELDS))
                .and(BaseSpecification.equals("typeDiscount", request.typeDiscount()))
                .and(BaseSpecification.equals("typeDisplay", request.typeDisplay()))
                .and(BaseSpecification.equals("status", request.status()))
                .and(BaseSpecification.withDateRange("createdAt", request.startDate(), request.endDate()));
    }

    private static Specification<Coupon> withFetch() {
        return (root, query, cb) -> {
            if (query != null && query.getResultType().equals(Coupon.class)) {
                root.fetch("createdBy", JoinType.LEFT);
                root.fetch("updatedBy", JoinType.LEFT);
            }
            return cb.conjunction();
        };
    }

}
