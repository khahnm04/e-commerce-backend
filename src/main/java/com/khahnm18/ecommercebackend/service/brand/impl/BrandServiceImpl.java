package com.khahnm18.ecommercebackend.service.brand.impl;

import com.khahnm18.ecommercebackend.enums.BrandStatus;
import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.brand.BrandRequest;
import com.khahnm18.ecommercebackend.dto.response.brand.BrandResponse;
import com.khahnm18.ecommercebackend.entity.brand.Brand;
import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.mapper.BrandMapper;
import com.khahnm18.ecommercebackend.repository.brand.BrandRepository;
import com.khahnm18.ecommercebackend.service.brand.BrandService;
import com.khahnm18.ecommercebackend.specification.BrandSpecification;
import com.khahnm18.ecommercebackend.util.PageableUtil;
import com.khahnm18.ecommercebackend.util.SecurityUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j(topic = "BRAND-SERVICE")
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandMapper brandMapper;
    private final BrandRepository brandRepository;

    @Override
    public BrandResponse getBrandDetailById(UUID brandId, boolean isDeleted) {
        Brand brand = isDeleted
            ? this.getSoftDeletedBrandById(brandId)
            : this.getBrandById(brandId);
        return brandMapper.toBrandResponse(brand);
    }

    @Override
    public Page<BrandResponse> getAllBrands(
        int page,
        int size,
        String sort,
        String keyword,
        boolean isDeleted,
        Instant startDate,
        Instant endDate,
        BrandStatus status
    ) {
        Pageable pageable = PageableUtil.createPageable(page, size, sort);

        Specification<Brand> spec = BrandSpecification.byIsDeleted(isDeleted)
            .and(BrandSpecification.byKeyword(keyword))
            .and(BrandSpecification.byStartDate(startDate))
            .and(BrandSpecification.byEndDate(endDate))
            .and(BrandSpecification.byStatus(status));

        return brandRepository.findAll(spec, pageable)
            .map(brandMapper::toBrandResponse);
    }

    @Override
    @Transactional
    public BrandResponse createBrand(BrandRequest request) {
        Brand brand = brandMapper.toBrand(request);
        brand = this.saveBrand(brand);
        log.info("Brand created: name={}, slug={}", brand.getName(), brand.getSlug());
        return brandMapper.toBrandResponse(brand);
    }

    @Override
    @Transactional
    public BrandResponse updateBrand(UUID brandId, BrandRequest request) {
        Brand brand = getBrandById(brandId);
        brandMapper.updateBrand(brand, request);
        brand = this.saveBrand(brand);
        log.info("Brand updated: brandId={}, name={}", brandId, brand.getName());
        return brandMapper.toBrandResponse(brand);
    }

    @Override
    @Transactional
    public void softDeleteBrand(UUID brandId) {
        Brand brand = getBrandById(brandId);
        this.softDelete(brand);
        log.info("Brand soft deleted with brand_id: {}", brandId);
    }

    @Override
    @Transactional
    public void restoreBrand(UUID brandId) {
        Brand brand = getSoftDeletedBrandById(brandId);
        this.restore(brand);
        log.info("Brand restored with brand_id: {}", brandId);
    }

    @Override
    @Transactional
    public void bulkAction(BulkActionRequest request) {
        Set<UUID> brandIds = request.ids();
        String action = request.action().toUpperCase();
        switch (action) {
            case "ACTIVE", "INACTIVE" -> {
                SecurityUtil.requireAuthority("BRAND_UPDATE");
                List<Brand> brands = brandRepository.findAllById(brandIds);
                brands.forEach(brand -> brand.setStatus(BrandStatus.valueOf(action)));
                log.info("Bulk update status {} brand with brandIds: {}", action, brandIds);
            }
            case "SOFT_DELETE" -> {
                SecurityUtil.requireAuthority("BRAND_TRASH");
                List<Brand> brands = brandRepository.findAllById(brandIds);
                brands.forEach(this::softDelete);
                log.info("Bulk action soft delete brand with brandIds: {}", brandIds);
            }
            case "RESTORE" -> {
                SecurityUtil.requireAuthority("BRAND_TRASH");
                List<Brand> brands = brandRepository.findAllById(brandIds);
                brands.forEach(this::restore);
                log.info("Bulk action restore brand with brandIds: {}", brandIds);
            }
            case "DELETE" -> {
                SecurityUtil.requireAuthority("BRAND_DELETE");
                brandRepository.deleteAllByIdInAndIsDeletedTrue(brandIds);
                log.info("Bulk action delete brand with brandIds: {}", brandIds);
            }
        }
    }

    @Override
    @Transactional
    public void deleteBrand(UUID brandId) {
        Brand brand = getSoftDeletedBrandById(brandId);
        brandRepository.delete(brand);
        log.info("Brand deleted success with brand_id: {}", brandId);
    }

    private Brand saveBrand(Brand brand) {
        try {
            return brandRepository.save(brand);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Brand save failed - duplicate entry: name={}, slug={}", brand.getName(), brand.getSlug());
            throw new AppException(ErrorCode.BRAND_EXISTED);
        }
    }

    private Brand getBrandById(UUID id) {
        return brandRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
    }

    private Brand getSoftDeletedBrandById(UUID id) {
        return brandRepository.findByIdAndIsDeletedTrue(id)
            .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
    }

    private void softDelete(Brand brand) {
        brand.setIsDeleted(true);
        brand.setDeletedAt(Instant.now());
        brand.setDeletedBy(SecurityUtil.getCurrentAccount());
    }

    private void restore(Brand brand) {
        brand.setIsDeleted(false);
        brand.setDeletedAt(null);
        brand.setDeletedBy(null);
    }

}
