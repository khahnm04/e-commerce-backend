package com.khahnm04.ecommerce.modules.coupon.dto.request;

import com.khahnm04.ecommerce.modules.coupon.enums.CouponDiscountType;
import com.khahnm04.ecommerce.modules.coupon.enums.CouponDisplayType;
import com.khahnm04.ecommerce.modules.coupon.enums.CouponStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.LocalDate;

@Builder
public record CouponFilterRequest(

    @Min(value = 1, message = "LIST_PAGE_MIN")
    Integer page,

    @Min(value = 1, message = "LIST_SIZE_MIN")
    @Max(value = 100, message = "LIST_SIZE_MAX")
    Integer size,

    String sort,
    String keyword,
    CouponDiscountType typeDiscount,
    CouponDisplayType typeDisplay,
    CouponStatus status,

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate startDate,

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate endDate

) implements Serializable {
    public CouponFilterRequest {
        if (page == null) page = 1;
        if (size == null) size = 10;
        if (sort == null) sort = "createdAt,desc";
    }
}
