package com.khahnm18.ecommercebackend.service.product.impl;

import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.product.ProductVersionRequest;
import com.khahnm18.ecommercebackend.dto.request.product.ProductVersionSearchRequest;
import com.khahnm18.ecommercebackend.dto.response.product.ProductVersionResponse;
import com.khahnm18.ecommercebackend.entity.product.ProductVersion;
import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.mapper.ProductVersionMapper;
import com.khahnm18.ecommercebackend.repository.product.ProductGroupRepository;
import com.khahnm18.ecommercebackend.repository.product.ProductVersionRepository;
import com.khahnm18.ecommercebackend.service.product.ProductVersionService;
import com.khahnm18.ecommercebackend.util.SecurityUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j(topic = "PRODUCT-VERSION-SERVICE")
@Service
@RequiredArgsConstructor
public class ProductVersionServiceImpl implements ProductVersionService {

    private final ProductVersionMapper productVersionMapper;
    private final ProductVersionRepository productVersionRepository;
    private final ProductGroupRepository productGroupRepository;

    @Override
    public ProductVersionResponse getProductVersionDetailById(UUID productVersionId, boolean isDeleted) {
        ProductVersion productVersion = isDeleted
            ? getSoftDeletedProductVersionById(productVersionId)
            : getProductVersionById(productVersionId);
        return productVersionMapper.toProductVersionResponse(productVersion);
    }

    @Override
    public Page<ProductVersionResponse> getAllProductVersions(
        int page,
        int size,
        ProductVersionSearchRequest request
    ) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size);
        return productVersionRepository
            .search(request, pageable)
            .map(productVersionMapper::toProductVersionResponse);
    }

    @Override
    @Transactional
    public ProductVersionResponse createProductVersion(ProductVersionRequest request) {
        getProductGroupById(request.groupId());
        ProductVersion productVersion = productVersionMapper.toProductVersion(request);
        productVersion = saveProductVersion(productVersion);
        log.info("Product version created: name={}, slug={}, groupId={}",
            productVersion.getName(), productVersion.getSlug(), productVersion.getGroupId());
        return productVersionMapper.toProductVersionResponse(productVersion);
    }

    @Override
    @Transactional
    public ProductVersionResponse updateProductVersion(UUID productVersionId, ProductVersionRequest request) {
        ProductVersion productVersion = getProductVersionById(productVersionId);
        if (request.groupId() != null) {
            getProductGroupById(request.groupId());
        }
        productVersionMapper.updateProductVersion(productVersion, request);
        productVersion = saveProductVersion(productVersion);
        log.info("Product version updated: productVersionId={}, name={}", productVersionId, productVersion.getName());
        return productVersionMapper.toProductVersionResponse(productVersion);
    }

    @Override
    @Transactional
    public void softDeleteProductVersion(UUID productVersionId) {
        ProductVersion productVersion = getProductVersionById(productVersionId);
        this.softDelete(productVersion);
        log.info("Product version soft deleted with product_version_id: {}", productVersionId);
    }

    @Override
    @Transactional
    public void restoreProductVersion(UUID productVersionId) {
        ProductVersion productVersion = getSoftDeletedProductVersionById(productVersionId);
        this.restore(productVersion);
        log.info("Product version restored with product_version_id: {}", productVersionId);
    }

    @Override
    @Transactional
    public void bulkAction(BulkActionRequest request) {
        Set<UUID> productVersionIds = request.ids();
        String action = request.action().toUpperCase();
        switch (action) {
            case "SOFT_DELETE" -> {
                SecurityUtil.requireAuthority("PRODUCT_VERSION_TRASH");
                List<ProductVersion> productVersions = productVersionRepository.findAllById(productVersionIds);
                productVersions.forEach(this::softDelete);
                log.info("Bulk action soft delete product versions with productVersionIds: {}", productVersionIds);
            }
            case "RESTORE" -> {
                SecurityUtil.requireAuthority("PRODUCT_VERSION_TRASH");
                List<ProductVersion> productVersions = productVersionRepository.findAllById(productVersionIds);
                productVersions.forEach(this::restore);
                log.info("Bulk action restore product versions with productVersionIds: {}", productVersionIds);
            }
            case "DELETE" -> {
                SecurityUtil.requireAuthority("PRODUCT_VERSION_DELETE");
                productVersionRepository.deleteAllByIdInAndIsDeletedTrue(productVersionIds);
                log.info("Bulk action delete product versions with productVersionIds: {}", productVersionIds);
            }
        }
    }

    @Override
    @Transactional
    public void deleteProductVersion(UUID productVersionId) {
        ProductVersion productVersion = getSoftDeletedProductVersionById(productVersionId);
        productVersionRepository.delete(productVersion);
        log.info("Product version deleted success with product_version_id: {}", productVersionId);
    }

    private ProductVersion saveProductVersion(ProductVersion productVersion) {
        try {
            return productVersionRepository.save(productVersion);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Product version save failed - duplicate entry: name={}, slug={}, groupId={}, version={}",
                productVersion.getName(), productVersion.getSlug(), productVersion.getGroupId(), productVersion.getVersion());
            throw new AppException(ErrorCode.PRODUCT_VERSION_EXISTED);
        }
    }

    private void getProductGroupById(UUID id) {
         productGroupRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_GROUP_NOT_FOUND));
    }

    private ProductVersion getProductVersionById(UUID id) {
        return productVersionRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VERSION_NOT_FOUND));
    }

    private ProductVersion getSoftDeletedProductVersionById(UUID id) {
        return productVersionRepository.findByIdAndIsDeletedTrue(id)
            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VERSION_NOT_FOUND));
    }

    private void softDelete(ProductVersion productVersion) {
        productVersion.setIsDeleted(true);
        productVersion.setDeletedAt(Instant.now());
        productVersion.setDeletedBy(SecurityUtil.getCurrentAccount());
    }

    private void restore(ProductVersion productVersion) {
        productVersion.setIsDeleted(false);
        productVersion.setDeletedAt(null);
        productVersion.setDeletedBy(null);
    }

}
