package com.khahnm04.ecommerce.service.coupon.impl;

import com.khahnm04.ecommerce.constant.coupon.CouponStatus;
import com.khahnm04.ecommerce.dto.projection.CouponProjection;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.coupon.CouponFilterRequest;
import com.khahnm04.ecommerce.dto.request.coupon.CouponRequest;
import com.khahnm04.ecommerce.dto.response.coupon.CouponResponse;
import com.khahnm04.ecommerce.entity.coupon.Coupon;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.coupon.CouponMapper;
import com.khahnm04.ecommerce.repository.coupon.CouponRepository;
import com.khahnm04.ecommerce.service.coupon.CouponService;
import com.khahnm04.ecommerce.specification.CouponSpecification;
import com.khahnm04.ecommerce.util.PageableUtil;
import com.khahnm04.ecommerce.util.SecurityUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.util.Set;

@Slf4j(topic = "COUPON-SERVICE")
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    @Override
    public CouponResponse createCoupon(CouponRequest request) {
        if (couponRepository.existsByCodeAndIsDeletedFalse(request.code())) {
            throw new AppException(ErrorCode.COUPON_EXISTED);
        }
        Coupon coupon = couponMapper.toCoupon(request);
        couponRepository.save(coupon);
        log.info("Coupon created: code={}, name={}", coupon.getCode(), coupon.getName());
        return couponMapper.toCouponResponse(coupon);
    }

    @Override
    public CouponResponse getCouponDetailById(Long id, boolean isDeleted) {
        Coupon coupon = isDeleted
            ? getSoftDeletedCouponById(id)
            : getCouponById(id);
        return couponMapper.toCouponResponse(coupon);
    }

    @Override
    public Page<CouponResponse> getAllCoupons(CouponFilterRequest request) {
        Pageable pageable = PageableUtil.createPageable(
            request.page(),
            request.size(),
            request.sort()
        );
        Page<CouponProjection> couponPage = couponRepository.findBy(
            CouponSpecification.buildQuery(request, request.isDeleted()),
            query -> query.as(CouponProjection.class).page(pageable)
        );
        return couponPage.map(couponMapper::toCouponResponse);
    }

    @Override
    @Transactional
    public CouponResponse updateCoupon(Long id, CouponRequest request) {
        if (couponRepository.existsByCodeAndIdNotAndIsDeletedFalse(request.code(), id)) {
            throw new AppException(ErrorCode.COUPON_EXISTED);
        }
        Coupon coupon = getCouponById(id);
        couponMapper.updateCoupon(coupon, request);
        log.info("Coupon updated: code={}, name={}", coupon.getCode(), coupon.getName());
        return couponMapper.toCouponResponse(coupon);
    }

    @Override
    @Transactional
    public void softDeleteCoupon(Long id) {
        Coupon coupon = getCouponById(id);
        coupon.setIsDeleted(true);
        coupon.setDeletedAt(Instant.now());
        coupon.setDeletedBy(SecurityUtil.getCurrentUser());
        couponRepository.save(coupon);
        log.info("Soft deleted coupon with id: {}", id);
    }

    @Override
    @Transactional
    public void deleteCoupon(Long id) {
        Coupon coupon = getSoftDeletedCouponById(id);
        couponRepository.delete(coupon);
        log.info("Deleted coupon with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreCoupon(Long id) {
        Coupon coupon = getSoftDeletedCouponById(id);
        coupon.setIsDeleted(false);
        coupon.setDeletedAt(null);
        coupon.setDeletedBy(null);
        couponRepository.save(coupon);
        log.info("Coupon restored with id: {}", id);
    }

    @Override
    @Transactional
    public void bulkAction(BulkActionRequest request) {
        Set<Long> couponIds = request.ids();
        String action = request.action().toUpperCase();
        switch (action) {
            case "ACTIVE", "INACTIVE" -> {
                couponRepository.updateStatusByIds(CouponStatus.valueOf(action), couponIds);
                log.info("Bulk update status {} coupons ids: {}", action, couponIds);
            }
            case "SOFT_DELETE" -> {
                couponRepository.softDeleteAllByIds(couponIds, SecurityUtil.getCurrentUser());
                log.info("Bulk action soft delete coupons ids: {}", couponIds);
            }
            case "RESTORE" -> {
                couponRepository.restoreAllByIds(couponIds);
                log.info("Bulk action restore coupons ids: {}", couponIds);
            }
            case "DELETE" -> {
                couponRepository.deleteAllByIds(couponIds);
                log.info("Bulk action delete coupons ids: {}", couponIds);
            }
        }
    }

    private Coupon getCouponById(Long id) {
        return couponRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new AppException(ErrorCode.COUPON_NOT_FOUND));
    }

    private Coupon getSoftDeletedCouponById(Long id) {
        return couponRepository.findByIdAndIsDeletedTrue(id)
            .orElseThrow(() -> new AppException(ErrorCode.COUPON_NOT_FOUND));
    }

}
