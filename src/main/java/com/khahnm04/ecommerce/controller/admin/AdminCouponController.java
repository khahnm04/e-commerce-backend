package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.config.Translator;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.coupon.CouponFilterRequest;
import com.khahnm04.ecommerce.dto.request.coupon.CouponRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.coupon.CouponResponse;
import com.khahnm04.ecommerce.service.coupon.CouponService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "COUPON-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/admin/coupons")
@RequiredArgsConstructor
@Tag(name = "Admin Coupon Controller", description = "APIs for managing coupons")
public class AdminCouponController {

    private final CouponService couponService;
    private final Translator translator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create coupon")
    @PreAuthorize("hasAuthority('COUPON_CREATE')")
    ApiResponse<CouponResponse> createCoupon(
        @Valid @RequestBody CouponRequest request
    ) {
        log.info("Create coupon");
        return ApiResponse.<CouponResponse>builder()
            .data(couponService.createCoupon(request))
            .message(translator.toLocale("COUPON_CREATED_SUCCESS"))
            .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get coupon detail")
    @PreAuthorize("hasAuthority('COUPON_VIEW')")
    ApiResponse<CouponResponse> getCouponDetailById(
        @PathVariable Long id,
        @RequestParam(defaultValue = "false") boolean isDeleted
    ) {
        log.info("Get coupon detail");
        return ApiResponse.<CouponResponse>builder()
            .data(couponService.getCouponDetailById(id, isDeleted))
            .message(translator.toLocale("COUPON_GET_DETAIL_SUCCESS"))
            .build();
    }

    @GetMapping
    @Operation(summary = "Get all coupons")
    @PreAuthorize("hasAuthority('COUPON_VIEW')")
    PageResponse<CouponResponse> getAllCoupons(
        @Valid @ModelAttribute CouponFilterRequest request
    ) {
        log.info("Get all coupons");
        Page<CouponResponse> page = couponService.getAllCoupons(request);

        return PageResponse.<CouponResponse>builder()
            .data(page.getContent())
            .meta(PageResponse.Meta.fromPage(page))
            .message(translator.toLocale("COUPON_GET_ALL_SUCCESS"))
            .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update coupon")
    @PreAuthorize("hasAuthority('COUPON_UPDATE')")
    ApiResponse<CouponResponse> updateCoupon(
        @PathVariable Long id,
        @Valid @RequestBody CouponRequest request
    ) {
        log.info("Update coupon");
        return ApiResponse.<CouponResponse>builder()
            .data(couponService.updateCoupon(id, request))
            .message(translator.toLocale("COUPON_UPDATED_SUCCESS"))
            .build();
    }

    @PatchMapping("/{id}/soft-delete")
    @Operation(summary = "Soft delete coupon")
    @PreAuthorize("hasAuthority('COUPON_DELETE')")
    ApiResponse<Void> softDeleteCoupon(
        @PathVariable Long id
    ) {
        log.info("Soft delete coupon");
        couponService.softDeleteCoupon(id);

        return ApiResponse.<Void>builder()
            .message(translator.toLocale("COUPON_SOFT_DELETED_SUCCESS"))
            .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete coupon")
    @PreAuthorize("hasAuthority('COUPON_DELETE')")
    ApiResponse<Void> deleteCoupon(
        @PathVariable Long id
    ) {
        log.info("Delete coupon");
        couponService.deleteCoupon(id);

        return ApiResponse.<Void>builder()
            .message(translator.toLocale("COUPON_DELETED_SUCCESS"))
            .build();
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Restore coupon")
    @PreAuthorize("hasAuthority('COUPON_DELETE')")
    ApiResponse<Void> restoreCoupon(
        @PathVariable Long id
    ) {
        log.info("Restore coupon");
        couponService.restoreCoupon(id);

        return ApiResponse.<Void>builder()
            .message(translator.toLocale("COUPON_RESTORED_SUCCESS"))
            .build();
    }

    @PatchMapping("/bulk")
    @Operation(summary = "Bulk action coupons")
    @PreAuthorize("hasAnyAuthority('COUPON_UPDATE', 'COUPON_DELETE')")
    ApiResponse<Void> bulkAction(
        @RequestBody BulkActionRequest request
    ) {
        couponService.bulkAction(request);

        return ApiResponse.<Void>builder()
            .message(translator.toLocale("COUPON_BULK_ACTION_SUCCESS"))
            .build();
    }

}
