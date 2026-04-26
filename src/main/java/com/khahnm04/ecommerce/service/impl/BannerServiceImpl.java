package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.common.enums.BannerStatus;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.config.AuditorAwareConfig;
import com.khahnm04.ecommerce.dto.projection.BannerProjection;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.BannerFilterRequest;
import com.khahnm04.ecommerce.dto.request.BannerRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.BannerResponse;
import com.khahnm04.ecommerce.entity.Banner;
import com.khahnm04.ecommerce.entity.Employee;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.BannerMapper;
import com.khahnm04.ecommerce.repository.BannerRepository;
import com.khahnm04.ecommerce.service.BannerService;
import com.khahnm04.ecommerce.specification.BannerSpecification;
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
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;
    private final AuditorAwareConfig auditorAwareConfig;

    @Override
    @Transactional
    public BannerResponse createBanner(BannerRequest request) {
        Banner banner = bannerMapper.toBanner(request);
        if (banner.getStatus() == null) {
            banner.setStatus(BannerStatus.ACTIVE);
        }
        Banner savedBanner = bannerRepository.save(banner);
        log.info("Created banner with id: {}", savedBanner.getId());
        return bannerMapper.toBannerResponse(savedBanner);
    }

    @Override
    public Banner getBannerById(Long id) {
        return bannerRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.BANNER_NOT_FOUND));
    }

    @Override
    public BannerResponse getBannerDetail(Long id, boolean deleted) {
        Banner banner = deleted
                ? getDeletedBannerById(id)
                : getBannerById(id);
        return bannerMapper.toBannerResponse(banner);
    }

    @Override
    public PageResponse<BannerResponse> getAllBanners(BannerFilterRequest request) {
        return getAllWithFilterAndSearch(request, false);
    }

    @Override
    public PageResponse<BannerResponse> getAllDeletedBanners(BannerFilterRequest request) {
        return getAllWithFilterAndSearch(request, true);
    }

    @Override
    @Transactional
    public BannerResponse updateBanner(Long id, BannerRequest request) {
        Banner banner = getBannerById(id);
        bannerMapper.updateBanner(banner, request);
        Banner savedBanner = bannerRepository.save(banner);
        log.info("Updated banner with id: {}", savedBanner.getId());
        return bannerMapper.toBannerResponse(savedBanner);
    }

    @Override
    @Transactional
    public void softDeleteBanner(Long id) {
        Banner banner = getBannerById(id);
        if (banner.getDeletedAt() != null) {
            log.warn("Banner with id {} is already soft deleted", id);
            return;
        }
        banner.setDeletedAt(LocalDateTime.now());
        banner.setDeletedBy(auditorAwareConfig.getCurrentAuditor().orElse(null));
        bannerRepository.save(banner);
        log.info("Soft-deleted banner with id: {}", id);
    }

    @Override
    @Transactional
    public void deleteBanner(Long id) {
        Banner banner = getDeletedBannerById(id);
        if (banner.getDeletedAt() == null) {
            log.warn("Banner with id {} is already deleted", id);
            return;
        }
        bannerRepository.delete(banner);
        log.info("Permanently deleted banner with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreBanner(Long id) {
        Banner banner = getDeletedBannerById(id);
        if (banner.getDeletedAt() == null) {
            log.warn("Banner with id {} is not in a deleted state", id);
            return;
        }
        banner.setDeletedAt(null);
        banner.setDeletedBy(null);
        bannerRepository.save(banner);
        log.info("Banner restored with id {}", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(BulkActionRequest request) {
        Set<Long> bannerIds = request.ids();
        String action = request.action().toUpperCase();

        if (bannerIds == null || bannerIds.isEmpty()) {
            throw new AppException(ErrorCode.BANNER_SELECTION_REQUIRED);
        }

        switch (action) {
            case "ACTIVE", "INACTIVE" -> {
                bannerRepository.updateStatusByIds(BannerStatus.valueOf(action), bannerIds);
                log.info("Bulk update status {} banners ids: {}", action, bannerIds);
            }
            case "SOFT_DELETE" -> {
                Employee currentAuditor = auditorAwareConfig.getCurrentAuditorReference();
                bannerRepository.softDeleteByIds(LocalDateTime.now(), currentAuditor, bannerIds);
                log.info("Bulk soft-delete banners ids: {}", bannerIds);
            }
            case "DELETE" -> {
                Set<Banner> banners = bannerRepository.findAllDeletedByIds(bannerIds);
                bannerRepository.deleteAll(banners);
                log.info("Bulk permanently delete banners ids: {}", bannerIds);
            }
            case "RESTORE" -> {
                bannerRepository.restoreDeletedByIds(bannerIds);
                log.info("Bulk restore banners ids: {}", bannerIds);
            }
            default -> throw new AppException(ErrorCode.BANNER_INVALID_BULK_ACTION);
        }
    }

    // ======================== Private methods ========================

    private Banner getDeletedBannerById(Long id) {
        return bannerRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.BANNER_NOT_FOUND));
    }

    private PageResponse<BannerResponse> getAllWithFilterAndSearch(BannerFilterRequest request, boolean isDeleted) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(), SortUtils.parseSort(request.sort()));

        Specification<Banner> spec = BannerSpecification.buildSpecification(request, isDeleted);
        Page<BannerProjection> page = bannerRepository
                .findBy(spec, q -> q.as(BannerProjection.class).page(pageable));

        List<BannerResponse> content = page.getContent().stream()
                .map(proj -> BannerResponse.builder()
                        .id(proj.getId())
                        .image(proj.getImage())
                        .position(proj.getPosition())
                        .sortOrder(proj.getSortOrder())
                        .label(proj.getLabel())
                        .heading(proj.getHeading())
                        .description(proj.getDescription())
                        .ctaText(proj.getCtaText())
                        .ctaUrl(proj.getCtaUrl())
                        .countdownEnd(proj.getCountdownEnd())
                        .status(proj.getStatus())
                        .createdAt(proj.getCreatedAt())
                        .createdBy(proj.getCreatedByFullName())
                        .updatedAt(proj.getUpdatedAt())
                        .updatedBy(proj.getUpdatedByFullName())
                        .deletedAt(proj.getDeletedAt())
                        .deletedBy(proj.getDeletedByFullName())
                        .build())
                .collect(Collectors.toList());

        Page<BannerResponse> responsePage = new PageImpl<>(content, pageable, page.getTotalElements());
        return PageResponse.fromPage(responsePage);
    }

}
