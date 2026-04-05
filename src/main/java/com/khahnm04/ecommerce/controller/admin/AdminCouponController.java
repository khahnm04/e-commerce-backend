package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.CouponFilterRequest;
import com.khahnm04.ecommerce.dto.request.CouponRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.CouponResponse;
import com.khahnm04.ecommerce.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/coupons")
@RequiredArgsConstructor
public class AdminCouponController {

    private final CouponService couponService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CouponResponse> createCoupon(
            @Valid @RequestBody CouponRequest request
    ) {
        return ApiResponse.<CouponResponse>builder()
                .data(couponService.createCoupon(request))
                .message("Coupon created successfully")
                .build();
    }

    @GetMapping("/{identifier}")
    public ApiResponse<CouponResponse> getCouponDetail(
            @PathVariable String identifier,
            @RequestParam(defaultValue = "false") boolean deleted) {
        return ApiResponse.<CouponResponse>builder()
                .data(couponService.getCouponDetail(identifier, deleted))
                .message("Get coupon detail successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<CouponResponse>> getAllCoupons(
            @Valid @ModelAttribute CouponFilterRequest filter) {
        PageResponse<CouponResponse> pageResponse = couponService.getAllCoupons(filter);
        return ApiResponse.<List<CouponResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get all coupons successfully")
                .build();
    }

    @GetMapping("/deleted")
    public ApiResponse<List<CouponResponse>> getAllDeletedCoupons(
            @Valid @ModelAttribute CouponFilterRequest filter) {
        PageResponse<CouponResponse> pageResponse = couponService.getAllDeletedCoupons(filter);
        return ApiResponse.<List<CouponResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get deleted coupons successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CouponResponse> updateCoupon(
            @PathVariable Long id,
            @Valid @RequestBody CouponRequest request) {
        return ApiResponse.<CouponResponse>builder()
                .data(couponService.updateCoupon(id, request))
                .message("Coupon updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDeleteCoupon(
            @PathVariable Long id) {
        couponService.softDeleteCoupon(id);
        return ApiResponse.<Void>builder()
                .message("Coupon soft deleted successfully")
                .build();
    }

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restoreCoupon(
            @PathVariable Long id) {
        couponService.restoreCoupon(id);
        return ApiResponse.<Void>builder()
                .message("Coupon restored successfully")
                .build();
    }

    @PatchMapping("/bulk")
    public ApiResponse<Void> handleBulkAction(
            @RequestBody BulkActionRequest request
    ) {
        couponService.handleBulkAction(request);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
