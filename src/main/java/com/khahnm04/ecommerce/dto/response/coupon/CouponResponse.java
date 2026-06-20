package com.khahnm04.ecommerce.dto.response.coupon;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.constant.coupon.CouponStatus;
import com.khahnm04.ecommerce.constant.coupon.CouponTypeDiscount;
import com.khahnm04.ecommerce.constant.coupon.CouponTypeDisplay;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CouponResponse(

    Long id,
    String code,
    String name,
    String description,
    BigDecimal value,
    BigDecimal minOrderValue,
    BigDecimal maxDiscountValue,
    Integer usageLimit,
    Integer usedCount,
    Instant startDate,
    Instant endDate,
    CouponTypeDiscount typeDiscount,
    CouponTypeDisplay typeDisplay,
    CouponStatus status,
    Instant createdAt,
    String createdBy,
    Instant updatedAt,
    String updatedBy,
    Instant deletedAt,
    String deletedBy,
    Boolean isDeleted

) implements Serializable {
}
