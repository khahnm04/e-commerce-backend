package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.common.enums.CouponStatus;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.config.AuditorAwareConfig;
import com.khahnm04.ecommerce.dto.projection.CouponProjection;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.CouponFilterRequest;
import com.khahnm04.ecommerce.dto.request.CouponRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.CouponResponse;
import com.khahnm04.ecommerce.entity.Coupon;
import com.khahnm04.ecommerce.entity.User;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.CouponMapper;
import com.khahnm04.ecommerce.repository.CouponRepository;
import com.khahnm04.ecommerce.repository.CouponUsageRepository;
import com.khahnm04.ecommerce.service.CouponService;
import com.khahnm04.ecommerce.specification.CouponSpecification;
import jakarta.persistence.EntityManager;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponUsageRepository couponUsageRepository;
    private final CouponMapper couponMapper;
    private final AuditorAwareConfig auditorAwareConfig;
    private final EntityManager entityManager;

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
    public void softDeleteCoupon(Long id) {
        Coupon coupon = getCouponById(id);
        if (coupon.getDeletedAt() != null) {
            log.warn("Coupon with id {} is already soft deleted", id);
            return;
        }
        coupon.setDeletedAt(LocalDateTime.now());
        coupon.setDeletedBy(auditorAwareConfig.getCurrentAuditor().orElse(null));
        couponRepository.save(coupon);
        log.info("Soft-deleted coupon with id: {}", id);
    }

    @Override
    @Transactional
    public void deleteCoupon(Long id) {
        Coupon coupon = getDeletedCouponById(id);
        if (coupon.getDeletedAt() == null) {
            log.warn("Coupon with id {} is already deleted", id);
            return;
        }
        if (couponUsageRepository.existsByCouponId(id)) {
            throw new AppException(ErrorCode.COUPON_HAS_USAGES);
        }
        couponRepository.delete(coupon);
        log.info("Permanently deleted coupon with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreCoupon(Long id) {
        Coupon coupon = getDeletedCouponById(id);
        if (coupon.getDeletedAt() == null) {
            log.warn("Coupon with id {} is not in a deleted state", id);
            return;
        }
        coupon.setDeletedAt(null);
        coupon.setDeletedBy(null);
        couponRepository.save(coupon);
        log.info("Restored coupon with id: {}", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(BulkActionRequest request) {
        Set<Long> couponIds = request.ids();
        String action = request.action().toUpperCase();

        if (couponIds == null || couponIds.isEmpty()) {
            throw new AppException(ErrorCode.COUPON_SELECTION_REQUIRED);
        }

        switch (action) {
            case "ACTIVE", "INACTIVE" -> {
                CouponStatus status = CouponStatus.valueOf(action);
                couponRepository.updateStatusByIds(status, couponIds);
            }
            case "SOFT_DELETE" -> {
                User currentAuditor = auditorAwareConfig.getCurrentAuditor()
                        .map(user -> entityManager.getReference(User.class, user.getId()))
                        .orElse(null);
                couponRepository.softDeleteByIds(LocalDateTime.now(), currentAuditor, couponIds);
                log.info("Bulk soft-delete coupons ids: {}", couponIds);
            }
            case "DELETE" -> {
                if (couponUsageRepository.existsByCouponIdIn(couponIds)) {
                    throw new AppException(ErrorCode.COUPON_HAS_USAGES);
                }
                Set<Coupon> coupons = couponRepository.findAllDeletedByIds(couponIds);
                couponRepository.deleteAll(coupons);
                log.info("Bulk permanently delete coupons ids: {}", couponIds);
            }
            case "RESTORE" -> couponRepository.restoreDeletedByIds(couponIds);
            default -> throw new AppException(ErrorCode.COUPON_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} coupons", action, couponIds.size());
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
        Page<CouponProjection> page = couponRepository.findBy(spec, q -> q.as(CouponProjection.class).page(pageable));

        List<CouponResponse> content = page.getContent().stream()
                .map(proj -> CouponResponse.builder()
                        .id(proj.getId())
                        .code(proj.getCode())
                        .name(proj.getName())
                        .description(proj.getDescription())
                        .typeDiscount(proj.getTypeDiscount())
                        .value(proj.getValue())
                        .minOrderValue(proj.getMinOrderValue())
                        .maxDiscountValue(proj.getMaxDiscountValue())
                        .usageLimit(proj.getUsageLimit())
                        .usedCount(proj.getUsedCount())
                        .startDate(proj.getStartDate())
                        .endDate(proj.getEndDate())
                        .typeDisplay(proj.getTypeDisplay())
                        .status(proj.getStatus())
                        .createdAt(proj.getCreatedAt())
                        .createdBy(proj.getCreatedByFullName())
                        .updatedAt(proj.getUpdatedAt())
                        .updatedBy(proj.getUpdatedByFullName())
                        .deletedAt(proj.getDeletedAt())
                        .deletedBy(proj.getDeletedByFullName())
                        .build())
                .collect(Collectors.toList());

        Page<CouponResponse> responsePage = new PageImpl<>(content, pageable, page.getTotalElements());
        return PageResponse.fromPage(responsePage);
    }

}
