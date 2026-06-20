package com.khahnm04.ecommerce.specification.coupon;

import com.khahnm04.ecommerce.constant.coupon.CouponStatus;
import com.khahnm04.ecommerce.constant.coupon.CouponTypeDiscount;
import com.khahnm04.ecommerce.constant.coupon.CouponTypeDisplay;
import com.khahnm04.ecommerce.dto.request.coupon.CouponFilterRequest;
import com.khahnm04.ecommerce.entity.coupon.Coupon;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class CouponSpecification {

    private static final String FIELD_IS_DELETED = "isDeleted";
    private static final String FIELD_CREATED_AT = "createdAt";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_TYPE_DISCOUNT = "typeDiscount";
    private static final String FIELD_TYPE_DISPLAY = "typeDisplay";
    private static final String[] KEYWORD_FIELDS = {"code", "name"};

    private CouponSpecification() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Specification<Coupon> buildQuery(CouponFilterRequest request, boolean isDeleted) {
        return byIsDeleted(isDeleted)
            .and(byKeyword(request.keyword()))
            .and(byStartDate(request.startDate()))
            .and(byEndDate(request.endDate()))
            .and(byStatus(request.status()))
            .and(byTypeDiscount(request.typeDiscount()))
            .and(byTypeDisplay(request.typeDisplay()));
    }

    private static Specification<Coupon> byIsDeleted(boolean isDeleted) {
        return (root, query, cb) ->
            cb.equal(root.get(FIELD_IS_DELETED), isDeleted);
    }

    private static Specification<Coupon> byKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) return null;
        return (root, query, cb) -> {
            String pattern = "%" + keyword.strip().toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();
            for (String field : KEYWORD_FIELDS) {
                predicates.add(cb.like(cb.lower(root.get(field)), pattern));
            }
            return cb.or(predicates.toArray(Predicate[]::new));
        };
    }

    private static Specification<Coupon> byStartDate(Instant startDate) {
        if (startDate == null) return null;
        return (root, query, cb) ->
            cb.greaterThanOrEqualTo(root.get(FIELD_CREATED_AT), startDate);
    }

    private static Specification<Coupon> byEndDate(Instant endDate) {
        if (endDate == null) return null;
        return (root, query, cb) ->
            cb.lessThanOrEqualTo(root.get(FIELD_CREATED_AT), endDate);
    }

    private static Specification<Coupon> byStatus(CouponStatus status) {
        if (status == null) return null;
        return (root, query, cb) ->
            cb.equal(root.get(FIELD_STATUS), status);
    }

    private static Specification<Coupon> byTypeDiscount(CouponTypeDiscount typeDiscount) {
        if (typeDiscount == null) return null;
        return (root, query, cb) ->
            cb.equal(root.get(FIELD_TYPE_DISCOUNT), typeDiscount);
    }

    private static Specification<Coupon> byTypeDisplay(CouponTypeDisplay typeDisplay) {
        if (typeDisplay == null) return null;
        return (root, query, cb) ->
            cb.equal(root.get(FIELD_TYPE_DISPLAY), typeDisplay);
    }

}
