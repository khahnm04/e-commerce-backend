package com.khahnm04.ecommerce.dto.request.coupon;

import com.khahnm04.ecommerce.constant.coupon.CouponStatus;
import com.khahnm04.ecommerce.constant.coupon.CouponTypeDiscount;
import com.khahnm04.ecommerce.constant.coupon.CouponTypeDisplay;
import lombok.Builder;
import java.io.Serializable;
import java.time.Instant;

@Builder
public record CouponFilterRequest(

    Integer page,
    Integer size,
    Boolean isDeleted,
    String sort,
    String keyword,
    Instant startDate,
    Instant endDate,
    CouponStatus status,
    CouponTypeDiscount typeDiscount,
    CouponTypeDisplay typeDisplay

) implements Serializable {
    public CouponFilterRequest {
        if (page == null) page = 1;
        if (size == null) size = 10;
        if (sort == null) sort = "createdAt,desc";
        if (isDeleted == null) isDeleted = false;
    }
}
