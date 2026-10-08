package com.khahnm18.ecommercebackend.service.product.impl;

import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.product.ProductRequest;
import com.khahnm18.ecommercebackend.dto.response.product.ProductResponse;
import com.khahnm18.ecommercebackend.entity.product.Product;
import com.khahnm18.ecommercebackend.enums.ProductStatus;
import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.mapper.ProductMapper;
import com.khahnm18.ecommercebackend.repository.product.ProductRepository;
import com.khahnm18.ecommercebackend.repository.product.ProductVersionRepository;
import com.khahnm18.ecommercebackend.service.product.ProductService;
import com.khahnm18.ecommercebackend.specification.ProductSpecification;
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

@Slf4j(topic = "PRODUCT-SERVICE")
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ProductVersionRepository productVersionRepository;

    @Override
    public ProductResponse getProductDetailById(UUID productId, boolean isDeleted) {
        Product product = isDeleted
            ? getSoftDeletedProductById(productId)
            : getProductById(productId);
        return productMapper.toProductResponse(product);
    }

    @Override
    public Page<ProductResponse> getAllProducts(
        int page,
        int size,
        String sort,
        String keyword,
        boolean isDeleted,
        Instant startDate,
        Instant endDate,
        ProductStatus status,
        UUID versionId
    ) {
        Pageable pageable = PageableUtil.createPageable(page, size, sort);

        Specification<Product> spec = ProductSpecification.byIsDeleted(isDeleted)
            .and(ProductSpecification.byKeyword(keyword))
            .and(ProductSpecification.byStartDate(startDate))
            .and(ProductSpecification.byEndDate(endDate))
            .and(ProductSpecification.byStatus(status))
            .and(ProductSpecification.byVersionId(versionId));

        return productRepository.findAll(spec, pageable)
            .map(productMapper::toProductResponse);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        getProductVersionById(request.versionId());
        Product product = productMapper.toProduct(request);
        product = this.saveProduct(product);
        log.info("Product created: name={}, slug={}, versionId={}",
            product.getName(), product.getSlug(), product.getVersionId());
        return productMapper.toProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(UUID productId, ProductRequest request) {
        Product product = getProductById(productId);
        if (request.versionId() != null) {
            this.getProductVersionById(request.versionId());
        }
        productMapper.updateProduct(product, request);
        product = this.saveProduct(product);
        log.info("Product updated: productId={}, name={}", productId, product.getName());
        return productMapper.toProductResponse(product);
    }

    @Override
    @Transactional
    public void softDeleteProduct(UUID productId) {
        Product product = this.getProductById(productId);
        softDelete(product);
        log.info("Product soft deleted with product_id: {}", productId);
    }

    @Override
    @Transactional
    public void restoreProduct(UUID productId) {
        Product product = this.getSoftDeletedProductById(productId);
        restore(product);
        log.info("Product restored with product_id: {}", productId);
    }

    @Override
    @Transactional
    public void bulkAction(BulkActionRequest request) {
        Set<UUID> productIds = request.ids();
        String action = request.action().toUpperCase();
        switch (action) {
            case "ACTIVE", "INACTIVE", "DRAFT" -> {
                SecurityUtil.requireAuthority("PRODUCT_UPDATE");
                List<Product> products = productRepository.findAllById(productIds);
                products.forEach(product -> product.setStatus(ProductStatus.valueOf(action)));
                log.info("Bulk update status {} products with productIds: {}", action, productIds);
            }
            case "SOFT_DELETE" -> {
                SecurityUtil.requireAuthority("PRODUCT_TRASH");
                List<Product> products = productRepository.findAllById(productIds);
                products.forEach(this::softDelete);
                log.info("Bulk action soft delete products with productIds: {}", productIds);
            }
            case "RESTORE" -> {
                SecurityUtil.requireAuthority("PRODUCT_TRASH");
                List<Product> products = productRepository.findAllById(productIds);
                products.forEach(this::restore);
                log.info("Bulk action restore products with productIds: {}", productIds);
            }
            case "DELETE" -> {
                SecurityUtil.requireAuthority("PRODUCT_DELETE");
                productRepository.deleteAllByIdInAndIsDeletedTrue(productIds);
                log.info("Bulk action delete products with productIds: {}", productIds);
            }
        }
    }

    @Override
    @Transactional
    public void deleteProduct(UUID productId) {
        Product product = getSoftDeletedProductById(productId);
        productRepository.delete(product);
        log.info("Product deleted success with product_id: {}", productId);
    }

    private Product saveProduct(Product product) {
        try {
            return productRepository.save(product);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Product save failed - duplicate entry: name={}, slug={}, sku={}",
                product.getName(), product.getSlug(), product.getSku());
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }
    }

    private void getProductVersionById(UUID id) {
        productVersionRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VERSION_NOT_FOUND));
    }

    private Product getProductById(UUID id) {
        return productRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private Product getSoftDeletedProductById(UUID id) {
        return productRepository.findByIdAndIsDeletedTrue(id)
            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private void softDelete(Product product) {
        product.setIsDeleted(true);
        product.setDeletedAt(Instant.now());
        product.setDeletedBy(SecurityUtil.getCurrentAccount());
    }

    private void restore(Product product) {
        product.setIsDeleted(false);
        product.setDeletedAt(null);
        product.setDeletedBy(null);
    }

}
