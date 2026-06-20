package com.khahnm04.ecommerce.dto.request.coupon;

import com.khahnm04.ecommerce.constant.coupon.CouponStatus;
import com.khahnm04.ecommerce.constant.coupon.CouponTypeDiscount;
import com.khahnm04.ecommerce.constant.coupon.CouponTypeDisplay;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public record CouponRequest(

    @NotBlank(message = "COUPON_CODE_REQUIRED")
    String code,

    @NotBlank(message = "COUPON_NAME_REQUIRED")
    String name,

    String description,

    @NotNull(message = "COUPON_VALUE_REQUIRED")
    BigDecimal value,

    BigDecimal minOrderValue,
    BigDecimal maxDiscountValue,
    Integer usageLimit,
    Integer usedCount,

    @NotNull(message = "COUPON_START_DATE_REQUIRED")
    Instant startDate,

    @NotNull(message = "COUPON_END_DATE_REQUIRED")
    Instant endDate,

    CouponTypeDiscount typeDiscount,
    CouponTypeDisplay typeDisplay,
    CouponStatus status

) implements Serializable {
}
