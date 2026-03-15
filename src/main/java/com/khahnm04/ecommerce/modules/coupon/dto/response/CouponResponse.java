package com.khahnm04.ecommerce.modules.coupon.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.modules.coupon.enums.CouponDiscountType;
import com.khahnm04.ecommerce.modules.coupon.enums.CouponDisplayType;
import com.khahnm04.ecommerce.modules.coupon.enums.CouponStatus;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CouponResponse(

    Long id,
    String code,
    String name,
    String description,
    CouponDiscountType typeDiscount,
    BigDecimal value,
    BigDecimal minOrderValue,
    BigDecimal maxDiscountValue,
    Integer usageLimit,
    Integer usedCount,
    LocalDateTime startDate,
    LocalDateTime endDate,
    CouponDisplayType typeDisplay,
    CouponStatus status,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime updatedAt,
    String updatedBy,
    LocalDateTime deletedAt

) implements Serializable {
}
