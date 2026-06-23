package com.khahnm04.ecommerce.mapper.coupon;

import com.khahnm04.ecommerce.constant.coupon.CouponStatus;
import com.khahnm04.ecommerce.constant.coupon.CouponTypeDiscount;
import com.khahnm04.ecommerce.constant.coupon.CouponTypeDisplay;
import com.khahnm04.ecommerce.dto.projection.CouponProjection;
import com.khahnm04.ecommerce.dto.request.coupon.CouponRequest;
import com.khahnm04.ecommerce.dto.response.coupon.CouponResponse;
import com.khahnm04.ecommerce.entity.coupon.Coupon;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class CouponMapper {

    public Coupon toCoupon(CouponRequest request) {
        return Coupon.builder()
            .code(request.code())
            .name(request.name())
            .description(request.description())
            .value(request.value())
            .minOrderValue(request.minOrderValue() == null ? BigDecimal.ZERO : request.minOrderValue())
            .maxDiscountValue(request.maxDiscountValue())
            .usageLimit(request.usageLimit())
            .usedCount(request.usedCount() == null ? 0 : request.usedCount())
            .startDate(request.startDate())
            .endDate(request.endDate())
            .typeDiscount(request.typeDiscount() == null ? CouponTypeDiscount.PERCENTAGE : request.typeDiscount())
            .typeDisplay(request.typeDisplay() == null ? CouponTypeDisplay.PRIVATE : request.typeDisplay())
            .status(request.status() == null ? CouponStatus.INACTIVE : request.status())
            .build();
    }

    public CouponResponse toCouponResponse(Coupon coupon) {
        return CouponResponse.builder()
            .id(coupon.getId())
            .code(coupon.getCode())
            .name(coupon.getName())
            .description(coupon.getDescription())
            .value(coupon.getValue())
            .minOrderValue(coupon.getMinOrderValue())
            .maxDiscountValue(coupon.getMaxDiscountValue())
            .usageLimit(coupon.getUsageLimit())
            .usedCount(coupon.getUsedCount())
            .startDate(coupon.getStartDate())
            .endDate(coupon.getEndDate())
            .typeDiscount(coupon.getTypeDiscount())
            .typeDisplay(coupon.getTypeDisplay())
            .status(coupon.getStatus())
            .createdAt(coupon.getCreatedAt())
            .createdBy(coupon.getCreatedBy())
            .updatedAt(coupon.getUpdatedAt())
            .updatedBy(coupon.getUpdatedBy())
            .deletedAt(coupon.getDeletedAt())
            .deletedBy(coupon.getDeletedBy())
            .isDeleted(coupon.getIsDeleted())
            .build();
    }

    public CouponResponse toCouponResponse(CouponProjection projection) {
        return CouponResponse.builder()
            .id(projection.getId())
            .code(projection.getCode())
            .name(projection.getName())
            .description(projection.getDescription())
            .value(projection.getValue())
            .minOrderValue(projection.getMinOrderValue())
            .maxDiscountValue(projection.getMaxDiscountValue())
            .usageLimit(projection.getUsageLimit())
            .usedCount(projection.getUsedCount())
            .startDate(projection.getStartDate())
            .endDate(projection.getEndDate())
            .typeDiscount(projection.getTypeDiscount())
            .typeDisplay(projection.getTypeDisplay())
            .status(projection.getStatus())
            .createdAt(projection.getCreatedAt())
            .createdBy(projection.getCreatedBy())
            .updatedAt(projection.getUpdatedAt())
            .updatedBy(projection.getUpdatedBy())
            .deletedAt(projection.getDeletedAt())
            .deletedBy(projection.getDeletedBy())
            .isDeleted(projection.getIsDeleted())
            .build();
    }

    public void updateCoupon(Coupon coupon, CouponRequest request) {
        if (request.code() != null) {
            coupon.setCode(request.code());
        }
        if (request.name() != null) {
            coupon.setName(request.name());
        }
        if (request.description() != null) {
            coupon.setDescription(request.description());
        }
        if (request.value() != null) {
            coupon.setValue(request.value());
        }
        if (request.minOrderValue() != null) {
            coupon.setMinOrderValue(request.minOrderValue());
        }
        if (request.maxDiscountValue() != null) {
            coupon.setMaxDiscountValue(request.maxDiscountValue());
        }
        if (request.usageLimit() != null) {
            coupon.setUsageLimit(request.usageLimit());
        }
        if (request.usedCount() != null) {
            coupon.setUsedCount(request.usedCount());
        }
        if (request.startDate() != null) {
            coupon.setStartDate(request.startDate());
        }
        if (request.endDate() != null) {
            coupon.setEndDate(request.endDate());
        }
        if (request.typeDiscount() != null) {
            coupon.setTypeDiscount(request.typeDiscount());
        }
        if (request.typeDisplay() != null) {
            coupon.setTypeDisplay(request.typeDisplay());
        }
        if (request.status() != null) {
            coupon.setStatus(request.status());
        }
    }

}
