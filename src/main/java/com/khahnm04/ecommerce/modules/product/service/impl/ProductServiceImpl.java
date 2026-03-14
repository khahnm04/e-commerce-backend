package com.khahnm04.ecommerce.modules.product.service.impl;

import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.common.base.entity.BaseEntity;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.common.util.SlugUtils;
import com.khahnm04.ecommerce.modules.brand.service.BrandService;
import com.khahnm04.ecommerce.modules.category.entity.Category;
import com.khahnm04.ecommerce.modules.category.repository.CategoryRepository;
import com.khahnm04.ecommerce.modules.product.dto.request.ProductFilterRequest;
import com.khahnm04.ecommerce.modules.product.dto.request.ProductRequest;
import com.khahnm04.ecommerce.modules.product.dto.request.ProductRequest.ProductAttributeValueRequest;
import com.khahnm04.ecommerce.modules.product.dto.request.ProductRequest.ProductImageRequest;
import com.khahnm04.ecommerce.modules.product.dto.response.ProductResponse;
import com.khahnm04.ecommerce.modules.product.entity.*;
import com.khahnm04.ecommerce.modules.product.enums.ProductStatus;
import com.khahnm04.ecommerce.modules.product.mapper.ProductMapper;
import com.khahnm04.ecommerce.modules.product.repository.*;
import com.khahnm04.ecommerce.modules.product.repository.specification.ProductSpecification;
import com.khahnm04.ecommerce.modules.product.service.ProductService;
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
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductAttributeValueRepository productAttributeValueRepository;
    private final ProductImageRepository productImageRepository;
    private final AttributeRepository attributeRepository;
    private final CategoryRepository categoryRepository;
    private final BrandService brandService;
    private final ProductMapper productMapper;

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
    public void updateProductStatus(Long id, ProductStatus status) {
        Product product = getProductById(id);
        if (product.getStatus() == status) {
            return;
        }
        product.setStatus(status);
        productRepository.save(product);
        log.info("Changed status for product id {} to {}", id, status);
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
        productRepository.save(product);
        log.info("Soft-deleted product with id: {}", id);
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
        productRepository.save(product);
        log.info("Product restored with id {}", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(Set<Long> productIds, String actionInput) {
        if (productIds == null || productIds.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_SELECTION_REQUIRED);
        }

        String action = actionInput.toUpperCase();

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
            case "ACTIVE", "INACTIVE", "INCOMING" -> {
                ProductStatus status = ProductStatus.valueOf(action);
                productRepository.updateStatusByIds(status, existingProductIds);
                log.info("Bulk update status {} products ids: {}", action, existingProductIds);
            }
            case "DELETE" -> {
                productRepository.softDeleteByIds(LocalDateTime.now(), existingProductIds);
                log.info("Bulk delete products ids: {}", existingProductIds);
            }
            case "RESTORE" -> {
                for (Product p : existingProducts) {
                    if (productRepository.existsByCodeAndDeletedAtIsNull(p.getCode())) {
                        throw new AppException(ErrorCode.PRODUCT_CODE_EXISTED);
                    }
                    if (productRepository.existsBySlugAndDeletedAtIsNull(p.getSlug())) {
                        throw new AppException(ErrorCode.PRODUCT_EXISTED);
                    }
                    if (productRepository.existsByNameAndDeletedAtIsNull(p.getName())) {
                        throw new AppException(ErrorCode.PRODUCT_EXISTED);
                    }
                }
                productRepository.restoreDeletedByIds(existingProductIds);
                log.info("Bulk restore products ids: {}", existingProductIds);
            }
            default -> throw new AppException(ErrorCode.PRODUCT_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} products", action, existingProductIds.size());
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
        Page<Product> page = productRepository.findAll(spec, pageable);

        List<ProductResponse> content = page.getContent().stream()
                .map(productMapper::toProductResponse)
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

    private void saveProductAttributeValues(Product product, List<ProductAttributeValueRequest> attributeRequests) {
        if (attributeRequests == null || attributeRequests.isEmpty()) {
            return;
        }

        Set<Long> attributeIds = attributeRequests.stream()
                .map(ProductAttributeValueRequest::attributeId)
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

    private void saveProductImages(Product product, List<ProductImageRequest> imageRequests) {
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

    private void mergeProductAttributeValues(Product product, List<ProductAttributeValueRequest> incomingAttributes) {
        Long productId = product.getId();

        if (incomingAttributes.isEmpty()) {
            productAttributeValueRepository.deleteAllByProductId(productId);
            return;
        }

        // Separate incoming into existing (has id) vs new (no id)
        List<ProductAttributeValueRequest> toUpdate = incomingAttributes.stream()
                .filter(req -> req.productAttributeValueId() != null)
                .toList();

        List<ProductAttributeValueRequest> toInsert = incomingAttributes.stream()
                .filter(req -> req.productAttributeValueId() == null)
                .toList();

        // Collect IDs to keep
        Set<Long> idsToKeep = toUpdate.stream()
                .map(ProductAttributeValueRequest::productAttributeValueId)
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

    private void mergeProductImages(Product product, List<ProductImageRequest> incomingImages) {
        Long productId = product.getId();

        if (incomingImages.isEmpty()) {
            productImageRepository.deleteAllByProductId(productId);
            return;
        }

        // Separate incoming into existing (has id) vs new (no id)
        List<ProductImageRequest> toUpdate = incomingImages.stream()
                .filter(req -> req.productImageId() != null)
                .toList();

        List<ProductImageRequest> toInsert = incomingImages.stream()
                .filter(req -> req.productImageId() == null)
                .toList();

        // Collect IDs to keep
        Set<Long> idsToKeep = toUpdate.stream()
                .map(ProductImageRequest::productImageId)
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
