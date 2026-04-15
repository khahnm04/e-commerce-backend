package com.khahnm04.ecommerce.dto.projection;

import com.khahnm04.ecommerce.common.enums.CouponDiscountType;
import com.khahnm04.ecommerce.common.enums.CouponDisplayType;
import com.khahnm04.ecommerce.common.enums.CouponStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CouponProjection {

    Long getId();

    String getCode();

    String getName();

    String getDescription();

    CouponDiscountType getTypeDiscount();

    BigDecimal getValue();

    BigDecimal getMinOrderValue();

    BigDecimal getMaxDiscountValue();

    Integer getUsageLimit();

    Integer getUsedCount();

    LocalDateTime getStartDate();

    LocalDateTime getEndDate();

    CouponDisplayType getTypeDisplay();

    CouponStatus getStatus();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    LocalDateTime getDeletedAt();

    String getCreatedByFullName();

    String getUpdatedByFullName();

    String getDeletedByFullName();

}
