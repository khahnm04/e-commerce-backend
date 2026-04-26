package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.common.enums.BrandStatus;
import com.khahnm04.ecommerce.common.util.SlugUtils;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.config.AuditorAwareConfig;
import com.khahnm04.ecommerce.dto.projection.BrandProjection;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.BrandFilterRequest;
import com.khahnm04.ecommerce.dto.request.BrandRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.BrandResponse;
import com.khahnm04.ecommerce.entity.Brand;
import com.khahnm04.ecommerce.entity.Employee;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.BrandMapper;
import com.khahnm04.ecommerce.repository.BrandRepository;
import com.khahnm04.ecommerce.repository.ProductRepository;
import com.khahnm04.ecommerce.service.BrandService;
import com.khahnm04.ecommerce.specification.BrandSpecification;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final BrandMapper brandMapper;
    private final AuditorAwareConfig auditorAwareConfig;

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
                && brandRepository.existsByNameAndIdNot(request.name(), id)) {
            throw new AppException(ErrorCode.BRAND_EXISTED);
        }
        if (StringUtils.hasText(request.slug())
                && brandRepository.existsBySlugAndIdNot(request.slug(), id)) {
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
        brand.setDeletedBy(auditorAwareConfig.getCurrentAuditor().orElse(null));
        brandRepository.save(brand);
        log.info("Soft-deleted brand with id: {}", id);
    }

    @Override
    @Transactional
    public void deleteBrand(Long id) {
        Brand brand = getDeletedBrandById(id);
        if (brand.getDeletedAt() == null) {
            log.warn("Brand with id {} is already deleted", id);
            return;
        }
        if (productRepository.existsByBrandId(id)) {
            throw new AppException(ErrorCode.BRAND_HAS_PRODUCTS);
        }
        brandRepository.delete(brand);
        log.info("Permanently deleted brand with id: {}", id);
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
        brand.setDeletedBy(null);
        brandRepository.save(brand);
        log.info("Brand restored with id {}", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(BulkActionRequest request) {
        Set<Long> brandIds = request.ids();
        String action = request.action().toUpperCase();

        if (brandIds == null || brandIds.isEmpty()) {
            throw new AppException(ErrorCode.BRAND_BULK_ACTION_REQUIRED_IDS);
        }

        switch (action) {
            case "ACTIVE", "INACTIVE" -> {
                brandRepository.updateStatusByIds(BrandStatus.valueOf(action), brandIds);
                log.info("Bulk update status {} brands ids: {}", action, brandIds);
            }
            case "SOFT_DELETE" -> {
                Employee currentAuditor = auditorAwareConfig.getCurrentAuditorReference();
                brandRepository.softDeleteByIds(LocalDateTime.now(), currentAuditor, brandIds);
                log.info("Bulk soft-delete brands ids: {}", brandIds);
            }
            case "DELETE" -> {
                if (productRepository.existsByBrandIdIn(brandIds)) {
                    throw new AppException(ErrorCode.BRAND_HAS_PRODUCTS);
                }
                Set<Brand> brands = brandRepository.findAllDeletedByIds(brandIds);
                brandRepository.deleteAll(brands);
                log.info("Bulk permanently delete brands ids: {}", brandIds);
            }
            case "RESTORE" -> {
                brandRepository.restoreDeletedByIds(brandIds);
                log.info("Bulk restore brands ids: {}", brandIds);
            }
            default -> throw new AppException(ErrorCode.BRAND_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} brands", action, brandIds.size());
    }

    // ======================== Private methods ========================

    private Brand getDeletedBrandById(Long id) {
        return brandRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
    }

    private PageResponse<BrandResponse> getAllWithFilterAndSearch(BrandFilterRequest request, boolean isDeleted) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(), SortUtils.parseSort(request.sort()));

        Specification<Brand> spec = BrandSpecification.buildSpecification(request, isDeleted);
        Page<BrandProjection> page = brandRepository
                .findBy(spec, q -> q.as(BrandProjection.class).page(pageable));

        List<BrandResponse> content = page.getContent().stream()
                .map(proj -> BrandResponse.builder()
                        .id(proj.getId())
                        .name(proj.getName())
                        .slug(proj.getSlug())
                        .description(proj.getDescription())
                        .logo(proj.getLogo())
                        .status(proj.getStatus())
                        .createdAt(proj.getCreatedAt())
                        .createdBy(proj.getCreatedByFullName())
                        .updatedAt(proj.getUpdatedAt())
                        .updatedBy(proj.getUpdatedByFullName())
                        .deletedAt(proj.getDeletedAt())
                        .deletedBy(proj.getDeletedByFullName())
                        .build())
                .collect(Collectors.toList());

        Page<BrandResponse> responsePage = new PageImpl<>(content, pageable, page.getTotalElements());
        return PageResponse.fromPage(responsePage);
    }

}
