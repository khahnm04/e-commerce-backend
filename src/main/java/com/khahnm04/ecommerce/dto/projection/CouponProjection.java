package com.khahnm04.ecommerce.dto.projection;

import com.khahnm04.ecommerce.constant.coupon.CouponStatus;
import com.khahnm04.ecommerce.constant.coupon.CouponTypeDiscount;
import com.khahnm04.ecommerce.constant.coupon.CouponTypeDisplay;
import java.math.BigDecimal;
import java.time.Instant;

public interface CouponProjection {

    Long getId();
    String getCode();
    String getName();
    String getDescription();
    CouponTypeDiscount getTypeDiscount();
    BigDecimal getValue();
    BigDecimal getMinOrderValue();
    BigDecimal getMaxDiscountValue();
    Integer getUsageLimit();
    Integer getUsedCount();
    Instant getStartDate();
    Instant getEndDate();
    CouponTypeDisplay getTypeDisplay();
    CouponStatus getStatus();
    Instant getCreatedAt();
    String getCreatedBy();
    Instant getUpdatedAt();
    String getUpdatedBy();
    Instant getDeletedAt();
    String getDeletedBy();
    Boolean getIsDeleted();

}
