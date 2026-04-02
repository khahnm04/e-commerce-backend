package com.khahnm04.ecommerce.dto.request.coupon;

import com.khahnm04.ecommerce.common.enums.CouponDiscountType;
import com.khahnm04.ecommerce.common.enums.CouponDisplayType;
import com.khahnm04.ecommerce.common.enums.CouponStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record CouponRequest(

    @NotBlank(message = "COUPON_CODE_REQUIRED")
    String code,

    @NotBlank(message = "COUPON_NAME_REQUIRED")
    String name,

    String description,

    @NotNull(message = "COUPON_DISC_TYPE_REQUIRED")
    CouponDiscountType typeDiscount,

    @NotNull(message = "COUPON_VALUE_REQUIRED")
    BigDecimal value,

    BigDecimal minOrderValue,
    BigDecimal maxDiscountValue,
    Integer usageLimit,

    @NotNull(message = "COUPON_START_DATE_REQUIRED")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime startDate,

    @NotNull(message = "COUPON_END_DATE_REQUIRED")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime endDate,

    @NotNull(message = "COUPON_DISPLAY_TYPE_REQUIRED")
    CouponDisplayType typeDisplay,

    CouponStatus status

) implements Serializable {
}
