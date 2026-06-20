package com.khahnm04.ecommerce.service.brand.impl;

import com.khahnm04.ecommerce.constant.brand.BrandStatus;
import com.khahnm04.ecommerce.dto.projection.brand.BrandProjection;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.brand.BrandFilterRequest;
import com.khahnm04.ecommerce.dto.request.brand.BrandRequest;
import com.khahnm04.ecommerce.dto.response.brand.BrandResponse;
import com.khahnm04.ecommerce.entity.brand.Brand;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.brand.BrandMapper;
import com.khahnm04.ecommerce.repository.brand.BrandRepository;
import com.khahnm04.ecommerce.service.brand.BrandService;
import com.khahnm04.ecommerce.specification.brand.BrandSpecification;
import com.khahnm04.ecommerce.util.PaginationUtil;
import com.khahnm04.ecommerce.util.SecurityUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.util.Set;

@Slf4j(topic = "BRAND-SERVICE")
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Override
    public BrandResponse createBrand(BrandRequest request) {
        if (brandRepository.existsBySlugAndIsDeletedFalse(request.slug())) {
            throw new AppException(ErrorCode.BRAND_EXISTED);
        }
        Brand brand = brandMapper.toBrand(request);
        brandRepository.save(brand);
        log.info("Brand created: slug={}, name={}", brand.getSlug(), brand.getName());
        return brandMapper.toBrandResponse(brand);
    }

    @Override
    public BrandResponse getBrandDetailById(Long id, boolean isDeleted) {
        Brand brand = isDeleted
            ? getSoftDeletedBrandById(id)
            : getBrandById(id);
        return brandMapper.toBrandResponse(brand);
    }

    @Override
    public Page<BrandResponse> getAllBrands(BrandFilterRequest request) {
        Pageable pageable = PaginationUtil.createPageable(
            request.page(),
            request.size(),
            request.sort()
        );
        Page<BrandProjection> brandPage = brandRepository.findBy(
            BrandSpecification.buildQuery(request, request.isDeleted()),
            query -> query.as(BrandProjection.class).page(pageable)
        );
        return brandPage.map(brandMapper::toBrandResponse);
    }

    @Override
    @Transactional
    public BrandResponse updateBrand(Long id, BrandRequest request) {
        if (brandRepository.existsBySlugAndIdNotAndIsDeletedFalse(request.slug(), id)) {
            throw new AppException(ErrorCode.BRAND_EXISTED);
        }
        Brand brand = getBrandById(id);
        brandMapper.updateBrand(brand, request);
        log.info("Brand updated: slug={}, name={}", brand.getSlug(), brand.getName());
        return brandMapper.toBrandResponse(brand);
    }

    @Override
    @Transactional
    public void softDeleteBrand(Long id) {
        Brand brand = getBrandById(id);
        brand.setIsDeleted(true);
        brand.setDeletedAt(Instant.now());
        brand.setDeletedBy(SecurityUtil.getCurrentUser());
        brandRepository.save(brand);
        log.info("Soft deleted brand with id: {}", id);
    }

    @Override
    @Transactional
    public void deleteBrand(Long id) {
        Brand brand = getSoftDeletedBrandById(id);
        brandRepository.delete(brand);
        log.info("Deleted brand with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreBrand(Long id) {
        Brand brand = getSoftDeletedBrandById(id);
        brand.setIsDeleted(false);
        brand.setDeletedAt(null);
        brand.setDeletedBy(null);
        brandRepository.save(brand);
        log.info("Brand restored with id: {}", id);
    }

    @Override
    @Transactional
    public void bulkAction(BulkActionRequest request) {
        Set<Long> brandIds = request.ids();
        String action = request.action().toUpperCase();
        switch (action) {
            case "ACTIVE", "INACTIVE" -> {
                brandRepository.updateStatusByIds(BrandStatus.valueOf(action), brandIds);
                log.info("Bulk update status {} brands ids: {}", action, brandIds);
            }
            case "SOFT_DELETE" -> {
                brandRepository.softDeleteAllByIds(brandIds, SecurityUtil.getCurrentUser());
                log.info("Bulk action soft delete brands ids: {}", brandIds);
            }
            case "RESTORE" -> {
                brandRepository.restoreAllByIds(brandIds);
                log.info("Bulk action restore brands ids: {}", brandIds);
            }
            case "DELETE" -> {
                brandRepository.deleteAllByIds(brandIds);
                log.info("Bulk action delete brands ids: {}", brandIds);
            }
        }
    }

    private Brand getBrandById(Long id) {
        return brandRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
    }

    private Brand getSoftDeletedBrandById(Long id) {
        return brandRepository.findByIdAndIsDeletedTrue(id)
            .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
    }

}
