package com.khahnm04.ecommerce.modules.catalog.service.impl;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.common.entity.BaseEntity;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.common.util.SlugUtils;
import com.khahnm04.ecommerce.modules.catalog.dto.request.BrandBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.BrandFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.BrandRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.BrandResponse;
import com.khahnm04.ecommerce.modules.catalog.entity.Brand;
import com.khahnm04.ecommerce.modules.catalog.enums.BrandSortField;
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
        if (brandRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.BRAND_EXISTED);
        }

        if (!StringUtils.hasText(request.getSlug())) {
            request.setSlug(SlugUtils.toSlug(request.getName()));
        }
        if (brandRepository.existsBySlug(request.getSlug())) {
            throw new AppException(ErrorCode.BRAND_EXISTED);
        }

        Brand brand = brandMapper.toBrand(request);
        Brand savedBrand = brandRepository.save(brand);
        log.info("Created brand with id: {}", savedBrand.getId());
        return brandMapper.toBrandResponse(savedBrand);
    }

    @Override
    public Brand getBrandById(Long id) {
        return brandRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
    }

    @Override
    public BrandResponse getBrandDetail(String identifier, boolean deleted) {
        Brand brand;

        if (identifier.matches("\\d+")) {
            Long id = Long.parseLong(identifier);
            brand = deleted
                    ? getDeletedBrandById(id)
                    : getBrandById(id);
        } else {
            brand = deleted
                    ? brandRepository.findBySlugAndDeletedAtIsNotNull(identifier)
                            .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND))
                    : brandRepository.findBySlugAndDeletedAtIsNull(identifier)
                            .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
        }

        return brandMapper.toBrandResponse(brand);
    }

    @Override
    public PageResponse<BrandResponse> getAllBrands(BrandFilterRequest request) {
        return getAllWithFilterAndSearch(request, false);
    }

    @Override
    public PageResponse<BrandResponse> getAllDeletedBrands(BrandFilterRequest request) {
        return getAllWithFilterAndSearch(request, true);
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
        log.info("Updated brand with id: {}", savedBrand.getId());
        return brandMapper.toBrandResponse(savedBrand);
    }

    @Override
    @Transactional
    public void softDeleteBrand(Long id) {
        Brand brand = getBrandById(id);
        if (brand.getDeletedAt() != null) {
            log.warn("Brand with id {} is already soft deleted", id);
            return;
        }
        brand.setDeletedAt(LocalDateTime.now());
        brandRepository.save(brand);
        log.info("Soft-deleted brand with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreBrand(Long id) {
        Brand brand = getDeletedBrandById(id);
        if (brand.getDeletedAt() == null) {
            log.warn("Brand with id {} is not in a deleted state", id);
            return;
        }
        brand.setDeletedAt(null);
        brandRepository.save(brand);
        log.info("Brand restored with id {}", id);
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

    private Brand getDeletedBrandById(Long id) {
        return brandRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
    }

    private PageResponse<BrandResponse> getAllWithFilterAndSearch(BrandFilterRequest request, boolean isDeleted) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim()
                : null;

        String[] sortParams = SortUtils.parseSortForMyBatis(request.getSort(), BrandSortField.class);
        String sortColumn = sortParams[0];
        String sortDirection = sortParams[1];

        List<BrandResponse> content = brandMyBatisMapper.findAllWithFilterAndSearch(
                keyword,
                request.getStartDate(),
                request.getEndDate(),
                isDeleted,
                sortColumn,
                sortDirection,
                pageable.getOffset(),
                pageable.getPageSize());

        long total = brandMyBatisMapper.countWithFilterAndSearch(
                keyword,
                request.getStartDate(),
                request.getEndDate(),
                isDeleted);

        Page<BrandResponse> page = new PageImpl<>(content, pageable, total);
        return PageResponse.fromPage(page);
    }

}
