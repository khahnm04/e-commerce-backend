package com.khahnm04.ecommerce.modules.catalog.service.impl;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.common.entity.BaseEntity;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.catalog.dto.request.*;
import com.khahnm04.ecommerce.modules.catalog.dto.response.ProductResponse;
import com.khahnm04.ecommerce.modules.catalog.dto.response.ProductResponse.ProductAttributeValueResponse;
import com.khahnm04.ecommerce.modules.catalog.dto.response.ProductResponse.ProductCategoryResponse;
import com.khahnm04.ecommerce.modules.catalog.dto.response.ProductResponse.ProductImageResponse;
import com.khahnm04.ecommerce.modules.catalog.entity.*;
import com.khahnm04.ecommerce.modules.catalog.enums.ProductSortField;
import com.khahnm04.ecommerce.modules.catalog.enums.ProductStatus;
import com.khahnm04.ecommerce.modules.catalog.mapper.ProductMapper;
import com.khahnm04.ecommerce.modules.catalog.mapper.mybaits.ProductMyBatisMapper;
import com.khahnm04.ecommerce.modules.catalog.repository.*;
import com.khahnm04.ecommerce.modules.catalog.service.BrandService;
import com.khahnm04.ecommerce.modules.catalog.service.ProductService;
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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductAttributeValueRepository productAttributeValueRepository;
    private final ProductImageRepository productImageRepository;
    private final AttributeRepository attributeRepository;
    private final CategoryRepository categoryRepository;
    private final BrandService brandService;
    private final ProductMapper productMapper;
    private final ProductMyBatisMapper productMyBatisMapper;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsBySlug(request.getSlug())) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }
        if (productRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }
        if (StringUtils.hasText(request.getProductCode())
                && productRepository.existsByProductCode(request.getProductCode())) {
            throw new AppException(ErrorCode.PRODUCT_CODE_EXISTED);
        }

        Product product = productMapper.toProduct(request);
        product.setBrand(brandService.getBrandById(request.getBrandId()));
        product.setStatus(request.getStatus() == null ? ProductStatus.ACTIVE : request.getStatus());

        Product savedProduct = productRepository.save(product);

        // Save categories
        saveProductCategories(savedProduct, request.getCategoryIds());

        // Save attribute values
        saveProductAttributeValues(savedProduct, request.getAttributes());

        // Save images
        saveProductImages(savedProduct, request.getImages());

        log.info("Created product id: {}", savedProduct.getId());
        return buildProductDetailResponse(savedProduct);
    }

    @Override
    public PageResponse<ProductResponse> getAllProducts(ProductFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<ProductResponse> page = this.getAllWithFilterAndSearch(request, pageable, false);
        return PageResponse.fromPage(page);
    }

    @Override
    public PageResponse<ProductResponse> getAllDeletedProducts(ProductFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<ProductResponse> page = this.getAllWithFilterAndSearch(request, pageable, true);
        return PageResponse.fromPage(page);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    @Override
    public ProductResponse getProductDetailById(Long id) {
        Product product = getProductById(id);
        return buildProductDetailResponse(product);
    }

    @Override
    public ProductResponse getProductDetailBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return buildProductDetailResponse(product);
    }

    @Override
    public ProductResponse getDeletedProductDetailById(Long id) {
        Product product = productRepository.findDeletedProductById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return buildProductDetailResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = getProductById(id);

        if (StringUtils.hasText(request.getName())
                && productRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }
        if (StringUtils.hasText(request.getSlug())
                && productRepository.existsBySlugAndIdNot(request.getSlug(), id)) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }
        if (StringUtils.hasText(request.getProductCode())
                && productRepository.existsByProductCodeAndIdNot(request.getProductCode(), id)) {
            throw new AppException(ErrorCode.PRODUCT_CODE_EXISTED);
        }

        productMapper.updateProduct(product, request);

        if (request.getBrandId() != null) {
            product.setBrand(brandService.getBrandById(request.getBrandId()));
        }

        Product savedProduct = productRepository.save(product);

        // Merge categories
        if (request.getCategoryIds() != null) {
            mergeProductCategories(savedProduct, request.getCategoryIds());
        }

        // Merge attribute values
        if (request.getAttributes() != null) {
            mergeProductAttributeValues(savedProduct, request.getAttributes());
        }

        // Merge images
        if (request.getImages() != null) {
            mergeProductImages(savedProduct, request.getImages());
        }

        log.info("Updated product id: {}", savedProduct.getId());
        return buildProductDetailResponse(savedProduct);
    }

    @Override
    @Transactional
    public void updateProductStatus(Long id, ProductUpdateStatusRequest request) {
        Product product = getProductById(id);
        if (product.getStatus() == request.getStatus()) {
            return;
        }
        product.setStatus(request.getStatus());
        productRepository.save(product);
        log.info("Changed status for product id {} to {}", id, request.getStatus());
    }

    @Override
    @Transactional
    public void softDeleteProduct(Long id) {
        Product product = getProductById(id);
        if (product.getDeletedAt() != null) {
            return;
        }
        product.setDeletedAt(LocalDateTime.now());
        productRepository.save(product);
        log.info("Soft deleted product id {}", id);
    }

    @Override
    @Transactional
    public void restoreProduct(Long id) {
        productRepository.findDeletedProductById(id)
                .ifPresentOrElse(product -> {
                    if (product.getDeletedAt() == null) {
                        log.warn("Product with id {} is not deleted, no restoration needed", id);
                        return;
                    }
                    product.setDeletedAt(null);
                    productRepository.save(product);
                    log.info("Restored product id {}", id);
                }, () -> {
                    throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
                });
    }

    @Override
    @Transactional
    public void handleBulkAction(ProductBulkActionRequest request) {
        Set<Long> productIds = request.getProducts();
        if (productIds == null || productIds.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_BULK_ACTION_REQUIRED_IDS);
        }

        String action = request.getAction().toUpperCase();

        Set<Product> existingProducts = new HashSet<>(
                action.equals("RESTORE")
                        ? productRepository.findAllDeletedByIds(productIds)
                        : productRepository.findAllById(productIds));

        Set<Long> existingProductIds = existingProducts.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        if (productIds.size() != existingProductIds.size()) {
            Set<Long> missingIds = new HashSet<>(productIds);
            missingIds.removeAll(existingProductIds);
            log.error("Bulk action products failed. Missing Ids: {}", missingIds);
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        switch (action) {
            case "ACTIVE", "INACTIVE", "OUT_OF_STOCK", "PENDING", "DISCONTINUED" -> {
                ProductStatus status = ProductStatus.valueOf(action);
                productRepository.updateStatusByIds(status, existingProductIds);
                log.info("Bulk update status {} products ids: {}", action, existingProductIds);
            }
            case "DELETE" -> {
                productRepository.softDeleteByIds(LocalDateTime.now(), existingProductIds);
                log.info("Bulk delete products ids: {}", existingProductIds);
            }
            case "RESTORE" -> {
                productRepository.restoreDeletedByIds(existingProductIds);
                log.info("Bulk restore products ids: {}", existingProductIds);
            }
            default -> throw new AppException(ErrorCode.PRODUCT_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} products", action, existingProductIds.size());
    }

    // ======================== Private methods ========================

    private Page<ProductResponse> getAllWithFilterAndSearch(ProductFilterRequest request, Pageable pageable,
            boolean isDeleted) {
        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim()
                : null;

        String[] sortParams = SortUtils.parseSortForMyBatis(request.getSort(), ProductSortField.class);
        String sortColumn = sortParams[0];
        String sortDirection = sortParams[1];

        List<ProductResponse> content = productMyBatisMapper.findAllWithFilterAndSearch(
                keyword,
                request.getStatus(),
                request.getBrandId(),
                request.getCategoryId(),
                request.getStartDate(),
                request.getEndDate(),
                isDeleted,
                sortColumn,
                sortDirection,
                pageable.getOffset(),
                pageable.getPageSize());

        long total = productMyBatisMapper.countWithFilterAndSearch(
                keyword,
                request.getStatus(),
                request.getBrandId(),
                request.getCategoryId(),
                request.getStartDate(),
                request.getEndDate(),
                isDeleted);

        return new PageImpl<>(content, pageable, total);
    }

    private List<Category> getALlCategoryByIds(Set<Long> ids) {
        List<Category> Categories = categoryRepository.findAllById(ids);
        if (Categories.size() != ids.size()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        return Categories;
    }

    private ProductResponse buildProductDetailResponse(Product product) {
        ProductResponse response = productMapper.toProductResponse(product);

        List<Long> productIds = List.of(product.getId());

        // Batch load categories
        response.setCategories(
                productCategoryRepository.findAllByProductIdInWithCategory(productIds)
                        .stream()
                        .map(pc -> ProductCategoryResponse.builder()
                                .id(pc.getId())
                                .categoryId(pc.getCategory().getId())
                                .categoryName(pc.getCategory().getName())
                                .build())
                        .toList());

        // Batch load attribute values
        response.setAttributes(
                productAttributeValueRepository.findAllByProductIdInWithAttribute(productIds)
                        .stream()
                        .map(pav -> ProductAttributeValueResponse.builder()
                                .id(pav.getId())
                                .attributeId(pav.getAttribute().getId())
                                .attributeName(pav.getAttribute().getName())
                                .value(pav.getValue())
                                .build())
                        .toList());

        // Batch load images
        response.setImages(
                productImageRepository.findAllByProductIdIn(productIds)
                        .stream()
                        .map(pi -> ProductImageResponse.builder()
                                .id(pi.getId())
                                .imageUrl(pi.getImageUrl())
                                .displayOrder(pi.getDisplayOrder())
                                .build())
                        .toList());

        return response;
    }

    private void saveProductCategories(Product product, Set<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return;
        }

        List<Category> categories = getALlCategoryByIds(categoryIds);
        List<ProductCategory> productCategories = categories.stream()
                .map(category -> ProductCategory.builder()
                        .product(product)
                        .category(category)
                        .build())
                .toList();

        productCategoryRepository.saveAll(productCategories);
    }

    private void saveProductAttributeValues(Product product, List<ProductAttributeValueRequest> attributeRequests) {
        if (attributeRequests == null || attributeRequests.isEmpty()) {
            return;
        }

        Set<Long> attributeIds = attributeRequests.stream()
                .map(ProductAttributeValueRequest::getAttributeId)
                .collect(Collectors.toSet());

        Map<Long, Attribute> attributeMap = attributeRepository.findAllById(attributeIds)
                .stream()
                .collect(Collectors.toMap(Attribute::getId, attribute -> attribute));

        if (attributeMap.size() != attributeIds.size()) {
            throw new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND);
        }

        List<ProductAttributeValue> values = attributeRequests.stream()
                .map(req -> ProductAttributeValue.builder()
                        .product(product)
                        .attribute(attributeMap.get(req.getAttributeId()))
                        .value(req.getValue())
                        .build())
                .toList();

        productAttributeValueRepository.saveAll(values);
    }

    private void saveProductImages(Product product, List<ProductImageRequest> imageRequests) {
        if (imageRequests == null || imageRequests.isEmpty()) {
            return;
        }

        List<ProductImage> images = imageRequests.stream()
                .map(req -> ProductImage.builder()
                        .product(product)
                        .imageUrl(req.getImageUrl())
                        .displayOrder(req.getDisplayOrder() != null ? req.getDisplayOrder() : 0)
                        .build())
                .toList();

        productImageRepository.saveAll(images);
    }

    private void mergeProductCategories(Product product, Set<Long> incomingCategoryIds) {
        Long productId = product.getId();

        if (incomingCategoryIds.isEmpty()) {
            productCategoryRepository.deleteAllByProductId(productId);
            return;
        }

        // Delete categories no longer in the request
        productCategoryRepository.deleteByProductIdAndCategoryIdNotIn(productId, incomingCategoryIds);

        // Find existing categoryIds for this product
        Set<Long> existingCategoryIds = productCategoryRepository.findCategoryIdsByProductId(productId);

        // Add only new categories
        Set<Long> newCategoryIds = new HashSet<>(incomingCategoryIds);
        newCategoryIds.removeAll(existingCategoryIds);

        if (!newCategoryIds.isEmpty()) {
            List<Category> newCategories = getALlCategoryByIds(newCategoryIds);
            List<ProductCategory> toInsert = newCategories.stream()
                    .map(category -> ProductCategory.builder()
                            .product(product)
                            .category(category)
                            .build())
                    .toList();

            productCategoryRepository.saveAll(toInsert);
        }
    }

    private void mergeProductAttributeValues(Product product, List<ProductAttributeValueRequest> incomingAttributes) {
        Long productId = product.getId();

        if (incomingAttributes.isEmpty()) {
            productAttributeValueRepository.deleteAllByProductId(productId);
            return;
        }

        // Separate incoming into existing (has id) vs new (no id)
        List<ProductAttributeValueRequest> toUpdate = incomingAttributes.stream()
                .filter(req -> req.getId() != null)
                .toList();

        List<ProductAttributeValueRequest> toInsert = incomingAttributes.stream()
                .filter(req -> req.getId() == null)
                .toList();

        // Collect IDs to keep
        Set<Long> idsToKeep = toUpdate.stream()
                .map(ProductAttributeValueRequest::getId)
                .collect(Collectors.toSet());

        // Delete records not in idsToKeep
        if (idsToKeep.isEmpty()) {
            productAttributeValueRepository.deleteAllByProductId(productId);
        } else {
            productAttributeValueRepository.deleteByProductIdAndIdNotIn(productId, idsToKeep);
        }

        // Update existing records
        if (!toUpdate.isEmpty()) {
            Map<Long, ProductAttributeValue> existingMap = productAttributeValueRepository.findAllById(idsToKeep)
                    .stream()
                    .collect(Collectors.toMap(ProductAttributeValue::getId, pav -> pav));

            for (ProductAttributeValueRequest req : toUpdate) {
                ProductAttributeValue existing = existingMap.get(req.getId());
                if (existing != null) {
                    existing.setValue(req.getValue());
                }
            }
            productAttributeValueRepository.saveAll(existingMap.values());
        }

        // Insert new records
        if (!toInsert.isEmpty()) {
            saveProductAttributeValues(product, toInsert);
        }
    }

    private void mergeProductImages(Product product, List<ProductImageRequest> incomingImages) {
        Long productId = product.getId();

        if (incomingImages.isEmpty()) {
            productImageRepository.deleteAllByProductId(productId);
            return;
        }

        // Separate incoming into existing (has id) vs new (no id)
        List<ProductImageRequest> toUpdate = incomingImages.stream()
                .filter(req -> req.getId() != null)
                .toList();

        List<ProductImageRequest> toInsert = incomingImages.stream()
                .filter(req -> req.getId() == null)
                .toList();

        // Collect IDs to keep
        Set<Long> idsToKeep = toUpdate.stream()
                .map(ProductImageRequest::getId)
                .collect(Collectors.toSet());

        // Delete records not in idsToKeep
        if (idsToKeep.isEmpty()) {
            productImageRepository.deleteAllByProductId(productId);
        } else {
            productImageRepository.deleteByProductIdAndIdNotIn(productId, idsToKeep);
        }

        // Update existing records
        if (!toUpdate.isEmpty()) {
            Map<Long, ProductImage> existingMap = productImageRepository.findAllById(idsToKeep)
                    .stream()
                    .collect(Collectors.toMap(ProductImage::getId, pi -> pi));

            for (ProductImageRequest req : toUpdate) {
                ProductImage existing = existingMap.get(req.getId());
                if (existing != null) {
                    existing.setImageUrl(req.getImageUrl());
                    existing.setDisplayOrder(req.getDisplayOrder() != null ? req.getDisplayOrder() : 0);
                }
            }
            productImageRepository.saveAll(existingMap.values());
        }

        // Insert new records
        if (!toInsert.isEmpty()) {
            saveProductImages(product, toInsert);
        }
    }

}
