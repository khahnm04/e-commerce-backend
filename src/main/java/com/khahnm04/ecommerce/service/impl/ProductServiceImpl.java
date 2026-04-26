package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.common.enums.ProductStatus;
import com.khahnm04.ecommerce.common.util.SlugUtils;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.config.AuditorAwareConfig;
import com.khahnm04.ecommerce.dto.projection.ProductProjection;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.ProductFilterRequest;
import com.khahnm04.ecommerce.dto.request.ProductRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.ProductResponse;
import com.khahnm04.ecommerce.entity.*;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.ProductMapper;
import com.khahnm04.ecommerce.repository.*;
import com.khahnm04.ecommerce.service.BrandService;
import com.khahnm04.ecommerce.service.ProductService;
import com.khahnm04.ecommerce.specification.ProductSpecification;
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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductAttributeValueRepository productAttributeValueRepository;
    private final ProductImageRepository productImageRepository;
    private final AttributeRepository attributeRepository;
    private final CategoryRepository categoryRepository;
    private final BrandService brandService;
    private final ProductMapper productMapper;
    private final AuditorAwareConfig auditorAwareConfig;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsByNameAndDeletedAtIsNull(request.name())) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }

        String slug = StringUtils.hasText(request.slug()) ? request.slug() : SlugUtils.toSlug(request.name());
        if (productRepository.existsBySlugAndDeletedAtIsNull(slug)) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }

        String code = StringUtils.hasText(request.code()) ? request.code() : slug.toUpperCase();
        if (productRepository.existsByCodeAndDeletedAtIsNull(code)) {
            throw new AppException(ErrorCode.PRODUCT_CODE_EXISTED);
        }

        Product product = productMapper.toProduct(request);
        product.setSlug(slug);
        product.setCode(code);
        product.setBrand(brandService.getBrandById(request.brandId()));
        product.setStatus(request.status() == null ? ProductStatus.ACTIVE : request.status());

        Product savedProduct = productRepository.save(product);

        // Save categories
        saveProductCategories(savedProduct, request.categories());

        // Save attribute values
        saveProductAttributeValues(savedProduct, request.attributes());

        // Save images
        saveProductImages(savedProduct, request.images());

        log.info("Created product with id: {}", savedProduct.getId());
        return buildProductDetailResponse(savedProduct);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    @Override
    public ProductResponse getProductDetail(String identifier, boolean deleted) {
        Product product;

        if (identifier.matches("\\d+")) {
            Long id = Long.parseLong(identifier);
            product = deleted
                    ? getDeletedProductById(id)
                    : getProductById(id);
        } else {
            product = deleted
                    ? productRepository.findBySlugAndDeletedAtIsNotNull(identifier)
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND))
                    : productRepository.findBySlugAndDeletedAtIsNull(identifier)
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        }

        return buildProductDetailResponse(product);
    }

    @Override
    public PageResponse<ProductResponse> getAllProducts(ProductFilterRequest request) {
        return getAllWithFilterAndSearch(request, false);
    }

    @Override
    public PageResponse<ProductResponse> getAllDeletedProducts(ProductFilterRequest request) {
        return getAllWithFilterAndSearch(request, true);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = getProductById(id);

        if (StringUtils.hasText(request.name())
                && productRepository.existsByNameAndIdNotAndDeletedAtIsNull(request.name(), id)) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }

        String slug = product.getSlug();
        if (StringUtils.hasText(request.slug())) {
            slug = request.slug();
            if (productRepository.existsBySlugAndIdNotAndDeletedAtIsNull(slug, id)) {
                throw new AppException(ErrorCode.PRODUCT_EXISTED);
            }
        }

        if (StringUtils.hasText(request.code())
                && productRepository.existsByCodeAndIdNotAndDeletedAtIsNull(request.code(), id)) {
            throw new AppException(ErrorCode.PRODUCT_CODE_EXISTED);
        }

        productMapper.updateProduct(product, request);
        product.setSlug(slug);

        if (request.brandId() != null) {
            product.setBrand(brandService.getBrandById(request.brandId()));
        }

        Product savedProduct = productRepository.save(product);

        // Merge categories
        if (request.categories() != null) {
            mergeProductCategories(savedProduct, request.categories());
        }

        // Merge attribute values
        if (request.attributes() != null) {
            mergeProductAttributeValues(savedProduct, request.attributes());
        }

        // Merge images
        if (request.images() != null) {
            mergeProductImages(savedProduct, request.images());
        }

        log.info("Updated product with id: {}", savedProduct.getId());
        return buildProductDetailResponse(savedProduct);
    }

    @Override
    @Transactional
    public void softDeleteProduct(Long id) {
        Product product = getProductById(id);
        if (product.getDeletedAt() != null) {
            log.warn("Product with id {} is already soft deleted", id);
            return;
        }
        product.setDeletedAt(LocalDateTime.now());
        product.setDeletedBy(auditorAwareConfig.getCurrentAuditor().orElse(null));
        productRepository.save(product);
        log.info("Soft-deleted product with id: {}", id);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = getDeletedProductById(id);
        if (product.getDeletedAt() == null) {
            log.warn("Product with id {} is already deleted", id);
            return;
        }
        if (productVariantRepository.existsByProductId(id)) {
            throw new AppException(ErrorCode.PRODUCT_HAS_VARIANTS);
        }

        productCategoryRepository.deleteAllByProductId(id);
        productAttributeValueRepository.deleteAllByProductId(id);
        productImageRepository.deleteAllByProductId(id);

        productRepository.delete(product);
        log.info("Permanently deleted product with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreProduct(Long id) {
        Product product = getDeletedProductById(id);
        if (product.getDeletedAt() == null) {
            log.warn("Product with id {} is not in a deleted state", id);
            return;
        }
        if (productRepository.existsByCodeAndDeletedAtIsNull(product.getCode())) {
            throw new AppException(ErrorCode.PRODUCT_CODE_EXISTED);
        }
        if (productRepository.existsBySlugAndDeletedAtIsNull(product.getSlug())) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }
        if (productRepository.existsByNameAndDeletedAtIsNull(product.getName())) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }
        product.setDeletedAt(null);
        product.setDeletedBy(null);
        productRepository.save(product);
        log.info("Product restored with id {}", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(BulkActionRequest request) {
        Set<Long> productIds = request.ids();
        String action = request.action().toUpperCase();

        if (productIds == null || productIds.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_SELECTION_REQUIRED);
        }

        switch (action) {
            case "ACTIVE", "INACTIVE", "INCOMING" -> {
                ProductStatus status = ProductStatus.valueOf(action);
                productRepository.updateStatusByIds(status, productIds);
                log.info("Bulk update status {} products ids: {}", action, productIds);
            }
            case "SOFT_DELETE" -> {
                Employee currentAuditor = auditorAwareConfig.getCurrentAuditorReference();
                productRepository.softDeleteByIds(LocalDateTime.now(), currentAuditor, productIds);
                log.info("Bulk soft-delete products ids: {}", productIds);
            }
            case "DELETE" -> {
                if (productVariantRepository.existsByProductIdIn(productIds)) {
                    throw new AppException(ErrorCode.PRODUCT_HAS_VARIANTS);
                }
                Set<Product> products = productRepository.findAllDeletedByIds(productIds);
                productRepository.deleteAll(products);
                log.info("Bulk permanently delete products ids: {}", productIds);
            }
            case "RESTORE" -> {
                productRepository.restoreDeletedByIds(productIds);
                log.info("Bulk restore products ids: {}", productIds);
            }
            default -> throw new AppException(ErrorCode.PRODUCT_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} products", action, productIds.size());
    }

    // ======================== Private methods ========================

    private Product getDeletedProductById(Long id) {
        return productRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private PageResponse<ProductResponse> getAllWithFilterAndSearch(ProductFilterRequest request, boolean isDeleted) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(),
                SortUtils.parseSort(request.sort()));

        Specification<Product> spec = ProductSpecification.buildSpecification(request, isDeleted);
        Page<ProductProjection> page = productRepository.findBy(spec,
                q -> q.as(ProductProjection.class).page(pageable));

        List<ProductResponse> content = page.getContent().stream()
                .map(proj -> ProductResponse.builder()
                        .id(proj.getId())
                        .name(proj.getName())
                        .slug(proj.getSlug())
                        .code(proj.getCode())
                        .brandName(proj.getBrandName())
                        .brandId(proj.getBrandId())
                        .warrantyMonths(proj.getWarrantyMonths())
                        .price(proj.getPrice())
                        .oldPrice(proj.getOldPrice())
                        .thumbnail(proj.getThumbnail())
                        .description(proj.getDescription())
                        .shortDescription(proj.getShortDescription())
                        .status(proj.getStatus())
                        .createdAt(proj.getCreatedAt())
                        .createdBy(proj.getCreatedByFullName())
                        .updatedAt(proj.getUpdatedAt())
                        .updatedBy(proj.getUpdatedByFullName())
                        .deletedAt(proj.getDeletedAt())
                        .deletedBy(proj.getDeletedByFullName())
                        .build())
                .collect(Collectors.toList());

        Page<ProductResponse> responsePage = new PageImpl<>(content, pageable, page.getTotalElements());
        return PageResponse.fromPage(responsePage);
    }

    private List<Category> getALlCategoryByIds(Set<Long> ids) {
        List<Category> categories = categoryRepository.findAllById(ids);
        if (categories.size() != ids.size()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        return categories;
    }

    private ProductResponse buildProductDetailResponse(Product product) {
        ProductResponse response = productMapper.toProductResponse(product);

        List<Long> productIds = List.of(product.getId());

        // Load categories
        List<ProductResponse.ProductCategoryResponse> categories = productCategoryRepository
                .findAllByProductIdInWithCategory(productIds)
                .stream()
                .map(pc -> new ProductResponse.ProductCategoryResponse(
                        pc.getId(),
                        pc.getCategory().getId(),
                        pc.getCategory().getName()))
                .toList();

        // Load attribute values
        List<ProductResponse.ProductAttributeValueResponse> attributes = productAttributeValueRepository
                .findAllByProductIdInWithAttribute(productIds)
                .stream()
                .map(pav -> new ProductResponse.ProductAttributeValueResponse(
                        pav.getId(),
                        pav.getAttribute().getId(),
                        pav.getAttribute().getName(),
                        pav.getValue()))
                .toList();

        // Load images
        List<ProductResponse.ProductImageResponse> images = productImageRepository.findAllByProductIdIn(productIds)
                .stream()
                .map(pi -> new ProductResponse.ProductImageResponse(
                        pi.getId(),
                        pi.getImage(),
                        pi.getPosition()))
                .toList();

        return response.toBuilder()
                .categories(categories)
                .attributes(attributes)
                .images(images)
                .build();
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
                .collect(Collectors.toList());

        productCategoryRepository.saveAll(productCategories);
    }

    private void saveProductAttributeValues(Product product,
            List<ProductRequest.ProductAttributeValueRequest> attributeRequests) {
        if (attributeRequests == null || attributeRequests.isEmpty()) {
            return;
        }

        Set<Long> attributeIds = attributeRequests.stream()
                .map(ProductRequest.ProductAttributeValueRequest::attributeId)
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
                        .attribute(attributeMap.get(req.attributeId()))
                        .value(req.value())
                        .build())
                .collect(Collectors.toList());

        productAttributeValueRepository.saveAll(values);
    }

    private void saveProductImages(Product product, List<ProductRequest.ProductImageRequest> imageRequests) {
        if (imageRequests == null || imageRequests.isEmpty()) {
            return;
        }

        List<ProductImage> images = imageRequests.stream()
                .map(req -> ProductImage.builder()
                        .product(product)
                        .image(req.image())
                        .position(req.position() != null ? req.position() : 0)
                        .build())
                .collect(Collectors.toList());

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
                    .collect(Collectors.toList());

            productCategoryRepository.saveAll(toInsert);
        }
    }

    private void mergeProductAttributeValues(Product product,
            List<ProductRequest.ProductAttributeValueRequest> incomingAttributes) {
        Long productId = product.getId();

        if (incomingAttributes.isEmpty()) {
            productAttributeValueRepository.deleteAllByProductId(productId);
            return;
        }

        // Separate incoming into existing (has id) vs new (no id)
        List<ProductRequest.ProductAttributeValueRequest> toUpdate = incomingAttributes.stream()
                .filter(req -> req.productAttributeValueId() != null)
                .toList();

        List<ProductRequest.ProductAttributeValueRequest> toInsert = incomingAttributes.stream()
                .filter(req -> req.productAttributeValueId() == null)
                .toList();

        // Collect IDs to keep
        Set<Long> idsToKeep = toUpdate.stream()
                .map(ProductRequest.ProductAttributeValueRequest::productAttributeValueId)
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

            for (ProductRequest.ProductAttributeValueRequest req : toUpdate) {
                ProductAttributeValue existing = existingMap.get(req.productAttributeValueId());
                if (existing != null) {
                    existing.setValue(req.value());
                }
            }
            productAttributeValueRepository.saveAll(existingMap.values());
        }

        // Insert new records
        if (!toInsert.isEmpty()) {
            saveProductAttributeValues(product, toInsert);
        }
    }

    private void mergeProductImages(Product product, List<ProductRequest.ProductImageRequest> incomingImages) {
        Long productId = product.getId();

        if (incomingImages.isEmpty()) {
            productImageRepository.deleteAllByProductId(productId);
            return;
        }

        // Separate incoming into existing (has id) vs new (no id)
        List<ProductRequest.ProductImageRequest> toUpdate = incomingImages.stream()
                .filter(req -> req.productImageId() != null)
                .toList();

        List<ProductRequest.ProductImageRequest> toInsert = incomingImages.stream()
                .filter(req -> req.productImageId() == null)
                .toList();

        // Collect IDs to keep
        Set<Long> idsToKeep = toUpdate.stream()
                .map(ProductRequest.ProductImageRequest::productImageId)
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

            for (ProductRequest.ProductImageRequest req : toUpdate) {
                ProductImage existing = existingMap.get(req.productImageId());
                if (existing != null) {
                    existing.setImage(req.image());
                    existing.setPosition(req.position() != null ? req.position() : 0);
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
