package com.khahnm04.ecommerce.entity.coupon;

import com.khahnm04.ecommerce.constant.coupon.CouponStatus;
import com.khahnm04.ecommerce.constant.coupon.CouponTypeDiscount;
import com.khahnm04.ecommerce.constant.coupon.CouponTypeDisplay;
import com.khahnm04.ecommerce.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupons")
public class Coupon extends BaseEntity {

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "value")
    private BigDecimal value;

    @Builder.Default
    @Column(name = "min_order_value")
    private BigDecimal minOrderValue = BigDecimal.ZERO;

    @Column(name = "max_discount_value")
    private BigDecimal maxDiscountValue;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Builder.Default
    @Column(name = "used_count")
    private Integer usedCount = 0;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "type_discount")
    private CouponTypeDiscount typeDiscount = CouponTypeDiscount.PERCENTAGE;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "type_display")
    private CouponTypeDisplay typeDisplay = CouponTypeDisplay.PRIVATE;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CouponStatus status = CouponStatus.INACTIVE;

}
