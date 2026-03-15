package com.khahnm04.ecommerce.modules.coupon.mapper;

import com.khahnm04.ecommerce.modules.coupon.dto.request.CouponRequest;
import com.khahnm04.ecommerce.modules.coupon.dto.response.CouponResponse;
import com.khahnm04.ecommerce.modules.coupon.entity.Coupon;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CouponMapper {

    Coupon toCoupon(CouponRequest request);

    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    CouponResponse toCouponResponse(Coupon coupon);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCoupon(@MappingTarget Coupon coupon, CouponRequest request);

}
