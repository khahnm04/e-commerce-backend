package com.khahnm18.ecommercebackend.service.product.impl;

import com.khahnm18.ecommercebackend.enums.BrandStatus;
import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.product.BrandRequest;
import com.khahnm18.ecommercebackend.dto.response.product.BrandResponse;
import com.khahnm18.ecommercebackend.entity.product.Brand;
import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.entity.product.QBrand;
import com.khahnm18.ecommercebackend.mapper.BrandMapper;
import com.khahnm18.ecommercebackend.repository.product.BrandRepository;
import com.khahnm18.ecommercebackend.service.product.BrandService;
import com.khahnm18.ecommercebackend.util.SecurityUtil;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j(topic = "BRAND-SERVICE")
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final JPAQueryFactory queryFactory;
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
        String keyword,
        boolean isDeleted,
        Instant startDate,
        Instant endDate,
        BrandStatus status
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        QBrand brand = QBrand.brand;

        Predicate[] predicates = {
            brand.isDeleted.eq(isDeleted),
            Optional.ofNullable(status).map(brand.status::eq).orElse(null),
            Optional.ofNullable(startDate).map(brand.createdAt::goe).orElse(null),
            Optional.ofNullable(endDate).map(brand.createdAt::loe).orElse(null),
            Optional.ofNullable(keyword)
                .filter(kw -> !kw.isBlank())
                .map(String::strip)
                .map(kw -> brand.name.containsIgnoreCase(kw)
                    .or(brand.slug.containsIgnoreCase(kw)))
                .orElse(null)
        };

        List<BrandResponse> content = queryFactory
            .selectFrom(brand)
            .where(predicates)
            .orderBy(brand.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch()
            .stream()
            .map(brandMapper::toBrandResponse)
            .toList();

        JPAQuery<Long> countQuery = queryFactory
            .select(brand.count())
            .from(brand)
            .where(predicates);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
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
