package com.khahnm04.ecommerce.service.coupon;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.coupon.CouponFilterRequest;
import com.khahnm04.ecommerce.dto.request.coupon.CouponRequest;
import com.khahnm04.ecommerce.dto.response.coupon.CouponResponse;
import org.springframework.data.domain.Page;

public interface CouponService {

    CouponResponse createCoupon(CouponRequest request);

    CouponResponse getCouponDetailById(Long id, boolean isDeleted);

    Page<CouponResponse> getAllCoupons(CouponFilterRequest request);

    CouponResponse updateCoupon(Long id, CouponRequest request);

    void softDeleteCoupon(Long id);

    void deleteCoupon(Long id);

    void restoreCoupon(Long id);

    void bulkAction(BulkActionRequest request);

}
