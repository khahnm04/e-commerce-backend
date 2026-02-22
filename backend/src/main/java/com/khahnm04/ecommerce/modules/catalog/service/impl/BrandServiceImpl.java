package com.khahnm04.ecommerce.modules.catalog.service.impl;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.common.entity.BaseEntity;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.catalog.dto.request.BrandBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.BrandFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.BrandRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.BrandUpdateStatusRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.BrandResponse;
import com.khahnm04.ecommerce.modules.catalog.entity.Brand;
import com.khahnm04.ecommerce.modules.catalog.enums.BrandSortField;
import com.khahnm04.ecommerce.modules.catalog.enums.BrandStatus;
import com.khahnm04.ecommerce.modules.catalog.mapper.BrandMapper;
import com.khahnm04.ecommerce.modules.catalog.mapper.mybaits.BrandMyBatisMapper;
import com.khahnm04.ecommerce.modules.catalog.repository.BrandRepository;
import com.khahnm04.ecommerce.modules.catalog.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final BrandMyBatisMapper brandMyBatisMapper;

    @Override
    @Transactional
    public BrandResponse createBrand(BrandRequest request) {
        if (brandRepository.existsBySlug(request.getSlug())) {
            throw new AppException(ErrorCode.BRAND_EXISTED);
        }
        if (brandRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.BRAND_EXISTED);
        }

        Brand brand = brandMapper.toBrand(request);
        brand.setStatus(request.getStatus() == null ? BrandStatus.ACTIVE : request.getStatus());

        Brand savedBrand = brandRepository.save(brand);
        log.info("Created brand id: {}", savedBrand.getId());
        return brandMapper.toBrandResponse(savedBrand);
    }

    @Override
    public PageResponse<BrandResponse> getAllBrands(BrandFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<BrandResponse> page = this.getAllWithFilterAndSearch(request, pageable, false);
        return PageResponse.fromPage(page);
    }

    @Override
    public PageResponse<BrandResponse> getAllDeletedBrands(BrandFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<BrandResponse> page = this.getAllWithFilterAndSearch(request, pageable, true);
        return PageResponse.fromPage(page);
    }

    @Override
    public Brand getBrandById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
    }

    @Override
    public BrandResponse getBrandDetailById(Long id) {
        return brandMapper.toBrandResponse(getBrandById(id));
    }

    @Override
    public BrandResponse getBrandDetailBySlug(String slug) {
        Brand brand = brandRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
        return brandMapper.toBrandResponse(brand);
    }

    @Override
    @Transactional
    public BrandResponse updateBrand(Long id, BrandRequest request) {
        Brand brand = getBrandById(id);

        if (StringUtils.hasText(request.getName())
                && brandRepository.existsByNameIgnoreCaseAndIdNot(request.getName(), id)) {
            throw new AppException(ErrorCode.BRAND_EXISTED);
        }
        if (StringUtils.hasText(request.getSlug())
                && brandRepository.existsBySlugIgnoreCaseAndIdNot(request.getSlug(), id)) {
            throw new AppException(ErrorCode.BRAND_EXISTED);
        }

        brandMapper.updateBrand(brand, request);
        Brand savedBrand = brandRepository.save(brand);
        log.info("Updated brand id: {}", savedBrand.getId());
        return brandMapper.toBrandResponse(savedBrand);
    }

    @Override
    @Transactional
    public void updateBrandStatus(Long id, BrandUpdateStatusRequest request) {
        Brand brand = getBrandById(id);
        if (brand.getStatus() == request.getStatus()) {
            return;
        }
        brand.setStatus(request.getStatus());
        brandRepository.save(brand);
        log.info("Changed status for brand id {} to {}", id, request.getStatus());
    }

    @Override
    @Transactional
    public void softDeleteBrand(Long id) {
        Brand brand = getBrandById(id);
        if (brand.getDeletedAt() != null) {
            return;
        }
        brand.setDeletedAt(LocalDateTime.now());
        brandRepository.save(brand);
        log.info("Soft deleted brand id {}", id);
    }

    @Override
    @Transactional
    public void restoreBrand(Long id) {
        brandRepository.findDeletedBrandById(id)
                .ifPresentOrElse(brand -> {
                    if (brand.getDeletedAt() == null) {
                        log.warn("Brand with id {} is not deleted, no restoration needed", id);
                        return;
                    }
                    brand.setDeletedAt(null);
                    brandRepository.save(brand);
                    log.info("Restored brand id {}", id);
                }, () -> {
                    throw new AppException(ErrorCode.BRAND_NOT_FOUND);
                });
    }

    @Override
    @Transactional
    public void handleBulkAction(BrandBulkActionRequest request) {
        Set<Long> brandIds = request.getBrands();
        if (brandIds == null || brandIds.isEmpty()) {
            throw new AppException(ErrorCode.BRAND_BULK_ACTION_REQUIRED_IDS);
        }

        String action = request.getAction().toUpperCase();

        Set<Brand> existingBrands = new HashSet<>(
                action.equals("RESTORE")
                        ? brandRepository.findAllDeletedByIds(brandIds)
                        : brandRepository.findAllById(brandIds));

        Set<Long> existingBrandIds = existingBrands.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        if (brandIds.size() != existingBrandIds.size()) {
            Set<Long> missingIds = new HashSet<>(brandIds);
            missingIds.removeAll(existingBrandIds);
            log.error("Bulk action brands failed. Missing Ids: {}", missingIds);
            throw new AppException(ErrorCode.BRAND_NOT_FOUND);
        }

        switch (action) {
            case "ACTIVE", "INACTIVE" -> {
                BrandStatus status = BrandStatus.valueOf(action);
                brandRepository.updateStatusByIds(status, existingBrandIds);
                log.info("Bulk update status {} brands ids: {}", action, existingBrandIds);
            }
            case "DELETE" -> {
                brandRepository.softDeleteByIds(LocalDateTime.now(), existingBrandIds);
                log.info("Bulk delete brands ids: {}", existingBrandIds);
            }
            case "RESTORE" -> {
                brandRepository.restoreDeletedByIds(existingBrandIds);
                log.info("Bulk restore brands ids: {}", existingBrandIds);
            }
            default -> throw new AppException(ErrorCode.BRAND_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} brands", action, existingBrandIds.size());
    }

    // ======================== Private methods ========================

    private Page<BrandResponse> getAllWithFilterAndSearch(BrandFilterRequest request, Pageable pageable,
            boolean isDeleted) {
        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim()
                : null;

        String[] sortParams = SortUtils.parseSortForMyBatis(request.getSort(), BrandSortField.class);
        String sortColumn = sortParams[0];
        String sortDirection = sortParams[1];

        List<BrandResponse> content = brandMyBatisMapper.findAllWithFilterAndSearch(
                keyword,
                request.getStatus(),
                request.getStartDate(),
                request.getEndDate(),
                isDeleted,
                sortColumn,
                sortDirection,
                pageable.getOffset(),
                pageable.getPageSize());

        long total = brandMyBatisMapper.countWithFilterAndSearch(
                keyword,
                request.getStatus(),
                request.getStartDate(),
                request.getEndDate(),
                isDeleted);

        return new PageImpl<>(content, pageable, total);
    }

}
