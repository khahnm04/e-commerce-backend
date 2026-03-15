package com.khahnm04.ecommerce.modules.coupon.service;

import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.modules.coupon.dto.request.CouponFilterRequest;
import com.khahnm04.ecommerce.modules.coupon.dto.request.CouponRequest;
import com.khahnm04.ecommerce.modules.coupon.dto.response.CouponResponse;
import com.khahnm04.ecommerce.modules.coupon.entity.Coupon;
import com.khahnm04.ecommerce.modules.coupon.enums.CouponStatus;
import java.util.Set;

public interface CouponService {

    CouponResponse createCoupon(CouponRequest request);

    Coupon getCouponById(Long id);

    CouponResponse getCouponDetail(String identifier, boolean deleted);

    PageResponse<CouponResponse> getAllCoupons(CouponFilterRequest filter);

    PageResponse<CouponResponse> getAllDeletedCoupons(CouponFilterRequest filter);

    CouponResponse updateCoupon(Long id, CouponRequest request);

    void updateCouponStatus(Long id, CouponStatus status);

    void softDeleteCoupon(Long id);

    void restoreCoupon(Long id);

    void handleBulkAction(Set<Long> couponIds, String action);

}
