package com.khahnm04.ecommerce.modules.coupon.service.impl;

import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.common.base.entity.BaseEntity;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.coupon.dto.request.CouponFilterRequest;
import com.khahnm04.ecommerce.modules.coupon.dto.request.CouponRequest;
import com.khahnm04.ecommerce.modules.coupon.dto.response.CouponResponse;
import com.khahnm04.ecommerce.modules.coupon.entity.Coupon;
import com.khahnm04.ecommerce.modules.coupon.enums.CouponStatus;
import com.khahnm04.ecommerce.modules.coupon.mapper.CouponMapper;
import com.khahnm04.ecommerce.modules.coupon.repository.CouponRepository;
import com.khahnm04.ecommerce.modules.coupon.repository.specification.CouponSpecification;
import com.khahnm04.ecommerce.modules.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    @Override
    @Transactional
    public CouponResponse createCoupon(CouponRequest request) {
        if (couponRepository.existsByCode(request.code())) {
            throw new AppException(ErrorCode.COUPON_EXISTED);
        }

        Coupon coupon = couponMapper.toCoupon(request);
        if (coupon.getStatus() == null) {
            coupon.setStatus(CouponStatus.INACTIVE);
        }

        Coupon saved = couponRepository.save(coupon);
        log.info("Created coupon with id: {} and code: {}", saved.getId(), saved.getCode());
        return couponMapper.toCouponResponse(saved);
    }

    @Override
    public Coupon getCouponById(Long id) {
        return couponRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.COUPON_NOT_FOUND));
    }

    @Override
    public CouponResponse getCouponDetail(String identifier, boolean deleted) {
        Coupon coupon;
        if (identifier.matches("\\d+")) {
            Long id = Long.parseLong(identifier);
            coupon = deleted
                    ? getDeletedCouponById(id)
                    : getCouponById(id);
        } else {
            coupon = deleted
                    ? couponRepository.findByCodeAndDeletedAtIsNull(identifier)
                            .filter(c -> c.getDeletedAt() != null)
                            .orElseThrow(() -> new AppException(ErrorCode.COUPON_NOT_FOUND))
                    : couponRepository.findByCodeAndDeletedAtIsNull(identifier)
                            .orElseThrow(() -> new AppException(ErrorCode.COUPON_NOT_FOUND));
        }
        return couponMapper.toCouponResponse(coupon);
    }

    @Override
    public PageResponse<CouponResponse> getAllCoupons(CouponFilterRequest request) {
        return getAllWithFilterAndSearch(request, false);
    }

    @Override
    public PageResponse<CouponResponse> getAllDeletedCoupons(CouponFilterRequest request) {
        return getAllWithFilterAndSearch(request, true);
    }

    @Override
    @Transactional
    public CouponResponse updateCoupon(Long id, CouponRequest request) {
        Coupon coupon = getCouponById(id);

        if (couponRepository.existsByCodeAndIdNot(request.code(), id)) {
            throw new AppException(ErrorCode.COUPON_EXISTED);
        }

        couponMapper.updateCoupon(coupon, request);
        Coupon saved = couponRepository.save(coupon);
        log.info("Updated coupon with id: {}", saved.getId());
        return couponMapper.toCouponResponse(saved);
    }

    @Override
    @Transactional
    public void updateCouponStatus(Long id, CouponStatus status) {
        Coupon coupon = getCouponById(id);
        coupon.setStatus(status);
        couponRepository.save(coupon);
        log.info("Updated coupon id: {} status to: {}", id, status);
    }

    @Override
    @Transactional
    public void softDeleteCoupon(Long id) {
        Coupon coupon = getCouponById(id);
        coupon.setDeletedAt(LocalDateTime.now());
        couponRepository.save(coupon);
        log.info("Soft-deleted coupon with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreCoupon(Long id) {
        Coupon coupon = getDeletedCouponById(id);
        coupon.setDeletedAt(null);
        couponRepository.save(coupon);
        log.info("Restored coupon with id: {}", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(Set<Long> couponIds, String actionInput) {
        if (couponIds == null || couponIds.isEmpty()) {
            throw new AppException(ErrorCode.COUPON_SELECTION_REQUIRED);
        }

        String action = actionInput.toUpperCase();

        Set<Coupon> existingCoupons = new HashSet<>(
                action.equals("RESTORE")
                        ? couponRepository.findAllDeletedByIds(couponIds)
                        : couponRepository.findAllById(couponIds));

        Set<Long> existingIds = existingCoupons.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        if (couponIds.size() != existingIds.size()) {
            Set<Long> missingIds = new HashSet<>(couponIds);
            missingIds.removeAll(existingIds);
            log.error("Bulk action coupon failed. Missing Ids: {}", missingIds);
            throw new AppException(ErrorCode.COUPON_NOT_FOUND);
        }

        switch (action) {
            case "ACTIVE", "INACTIVE" -> {
                CouponStatus status = CouponStatus.valueOf(action);
                couponRepository.updateStatusByIds(status, existingIds);
            }
            case "DELETE" -> couponRepository.softDeleteByIds(LocalDateTime.now(), existingIds);
            case "RESTORE" -> couponRepository.restoreDeletedByIds(existingIds);
            default -> throw new AppException(ErrorCode.COUPON_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} coupons", action, existingIds.size());
    }

    // ======================== Private methods ========================

    private Coupon getDeletedCouponById(Long id) {
        return couponRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.COUPON_NOT_FOUND));
    }

    private PageResponse<CouponResponse> getAllWithFilterAndSearch(CouponFilterRequest request, boolean isDeleted) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(),
                SortUtils.parseSort(request.sort()));

        Specification<Coupon> spec = CouponSpecification.buildSpecification(request, isDeleted);
        Page<Coupon> page = couponRepository.findAll(spec, pageable);

        List<CouponResponse> content = page.getContent().stream()
                .map(couponMapper::toCouponResponse)
                .collect(Collectors.toList());

        Page<CouponResponse> responsePage = new PageImpl<>(content, pageable, page.getTotalElements());
        return PageResponse.fromPage(responsePage);
    }

}
