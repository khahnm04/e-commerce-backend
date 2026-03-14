package com.khahnm04.ecommerce.modules.brand.service.impl;

import com.khahnm04.ecommerce.common.base.entity.BaseEntity;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.common.util.SlugUtils;
import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.modules.brand.dto.request.BrandFilterRequest;
import com.khahnm04.ecommerce.modules.brand.dto.request.BrandRequest;
import com.khahnm04.ecommerce.modules.brand.dto.response.BrandResponse;
import com.khahnm04.ecommerce.modules.brand.entity.Brand;
import com.khahnm04.ecommerce.modules.brand.enums.BrandStatus;
import com.khahnm04.ecommerce.modules.brand.mapper.BrandMapper;
import com.khahnm04.ecommerce.modules.brand.repository.BrandRepository;
import com.khahnm04.ecommerce.modules.brand.repository.specification.BrandSpecification;
import com.khahnm04.ecommerce.modules.brand.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    @Override
    @Transactional
    public BrandResponse createBrand(BrandRequest request) {
        if (brandRepository.existsByName(request.name())) {
            throw new AppException(ErrorCode.BRAND_EXISTED);
        }

        String slug = StringUtils.hasText(request.slug())
                ? request.slug()
                : SlugUtils.toSlug(request.name());

        if (brandRepository.existsBySlug(slug)) {
            throw new AppException(ErrorCode.BRAND_EXISTED);
        }

        Brand brand = brandMapper.toBrand(request);
        brand.setSlug(slug);
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

        if (StringUtils.hasText(request.name())
                && brandRepository.existsByNameIgnoreCaseAndIdNot(request.name(), id)) {
            throw new AppException(ErrorCode.BRAND_EXISTED);
        }
        if (StringUtils.hasText(request.slug())
                && brandRepository.existsBySlugIgnoreCaseAndIdNot(request.slug(), id)) {
            throw new AppException(ErrorCode.BRAND_EXISTED);
        }

        brandMapper.updateBrand(brand, request);
        Brand savedBrand = brandRepository.save(brand);
        log.info("Updated brand with id: {}", savedBrand.getId());
        return brandMapper.toBrandResponse(savedBrand);
    }

    @Override
    @Transactional
    public void updateBrandStatus(Long id, BrandStatus status) {
        Brand brand = getBrandById(id);
        if (brand.getStatus() == status) {
            return;
        }
        brand.setStatus(status);
        brandRepository.save(brand);
        log.info("Updated status for brand {} to {}", id, status);
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
    public void handleBulkAction(Set<Long> brandIds, String actionInput) {
        if (brandIds == null || brandIds.isEmpty()) {
            throw new AppException(ErrorCode.BRAND_BULK_ACTION_REQUIRED_IDS);
        }

        String action = actionInput.toUpperCase();

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
                brandRepository.updateStatusByIds(BrandStatus.valueOf(action), existingBrandIds);
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

    private Brand getDeletedBrandById(Long id) {
        return brandRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
    }

    private PageResponse<BrandResponse> getAllWithFilterAndSearch(BrandFilterRequest request, boolean isDeleted) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(),
                SortUtils.parseSort(request.sort()));

        Specification<Brand> spec = BrandSpecification.buildSpecification(request, isDeleted);
        Page<Brand> page = brandRepository.findAll(spec, pageable);

        List<BrandResponse> content = page.getContent().stream()
                .map(brandMapper::toBrandResponse)
                .collect(Collectors.toList());

        Page<BrandResponse> responsePage = new PageImpl<>(content, pageable, page.getTotalElements());
        return PageResponse.fromPage(responsePage);
    }

}
