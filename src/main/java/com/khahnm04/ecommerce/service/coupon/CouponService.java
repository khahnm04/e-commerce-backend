package com.khahnm04.ecommerce.service.coupon;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.coupon.CouponFilterRequest;
import com.khahnm04.ecommerce.dto.request.coupon.CouponRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.coupon.CouponResponse;
import com.khahnm04.ecommerce.entity.coupon.Coupon;

public interface CouponService {

    CouponResponse createCoupon(CouponRequest request);

    Coupon getCouponById(Long id);

    CouponResponse getCouponDetail(String identifier, boolean deleted);

    PageResponse<CouponResponse> getAllCoupons(CouponFilterRequest filter);

    PageResponse<CouponResponse> getAllDeletedCoupons(CouponFilterRequest filter);

    CouponResponse updateCoupon(Long id, CouponRequest request);

    void softDeleteCoupon(Long id);

    void restoreCoupon(Long id);

    void handleBulkAction(BulkActionRequest request);

}
