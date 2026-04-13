package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.CouponFilterRequest;
import com.khahnm04.ecommerce.dto.request.CouponRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.CouponResponse;
import com.khahnm04.ecommerce.entity.Coupon;

public interface CouponService {

    CouponResponse createCoupon(CouponRequest request);

    Coupon getCouponById(Long id);

    CouponResponse getCouponDetail(String identifier, boolean deleted);

    PageResponse<CouponResponse> getAllCoupons(CouponFilterRequest filter);

    PageResponse<CouponResponse> getAllDeletedCoupons(CouponFilterRequest filter);

    CouponResponse updateCoupon(Long id, CouponRequest request);

    void softDeleteCoupon(Long id);

    void deleteCoupon(Long id);

    void restoreCoupon(Long id);

    void handleBulkAction(BulkActionRequest request);

}
