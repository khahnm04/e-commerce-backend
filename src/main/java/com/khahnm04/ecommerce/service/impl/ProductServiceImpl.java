package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.common.enums.ProductStatus;
import com.khahnm04.ecommerce.common.enums.ProductVariantStatus;
import com.khahnm04.ecommerce.common.util.SlugUtils;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.config.AuditorAwareConfig;
import com.khahnm04.ecommerce.dto.projection.ProductProjection;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.ProductRequest;
import com.khahnm04.ecommerce.dto.request.ProductVariantRequest;
import com.khahnm04.ecommerce.dto.request.ProductImageRequest;
import com.khahnm04.ecommerce.dto.request.filter.ProductFilterRequest;
import com.khahnm04.ecommerce.dto.request.filter.ProductVariantFilterRequest;
import com.khahnm04.ecommerce.dto.response.*;
import com.khahnm04.ecommerce.entity.*;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.ProductMapper;
import com.khahnm04.ecommerce.repository.*;
import com.khahnm04.ecommerce.repository.product.*;
import com.khahnm04.ecommerce.service.BrandService;
import com.khahnm04.ecommerce.service.InventoryService;
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
import com.khahnm04.ecommerce.dto.projection.ProductVariantProjection;
import com.khahnm04.ecommerce.specification.ProductVariantSpecification;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductTagRepository productTagRepository;
    private final ProductImageRepository productImageRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final VariantValueRepository variantValueRepository;
    private final ProductVariantValueRepository productVariantValueRepository;
    private final BrandService brandService;
    private final InventoryService inventoryService;
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
        product.setIsFeatured(request.isFeatured() != null && request.isFeatured());
        product.setIsTrending(request.isTrending() != null && request.isTrending());

        Product savedProduct = productRepository.save(product);

        // Save categories
        saveProductCategories(savedProduct, request.categories());

        // Save tags
        saveProductTags(savedProduct, request.tags());

        // Save images
        saveProductImages(savedProduct, request.images());

        // Save product variants
        saveProductVariants(savedProduct, request.productVariants());

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

        if (request.isFeatured() != null) {
            product.setIsFeatured(request.isFeatured());
        }

        if (request.isTrending() != null) {
            product.setIsTrending(request.isTrending());
        }

        Product savedProduct = productRepository.save(product);

        // Merge categories
        if (request.categories() != null) {
            mergeProductCategories(savedProduct, request.categories());
        }

        // Merge tags
        if (request.tags() != null) {
            mergeProductTags(savedProduct, request.tags());
        }

        // Merge images
        if (request.images() != null) {
            mergeProductImages(savedProduct, request.images());
        }

        // Merge product variants
        if (request.productVariants() != null) {
            mergeProductVariants(savedProduct, request.productVariants());
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
        productTagRepository.deleteAllByProductId(id);
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

    @Override
    public PageResponse<ProductVariantResponse> getAllProductVariants(ProductVariantFilterRequest filter) {
        return getAllVariantsWithFilter(filter, false);
    }

    @Override
    public PageResponse<ProductVariantResponse> getAllDeletedProductVariants(ProductVariantFilterRequest filter) {
        return getAllVariantsWithFilter(filter, true);
    }

    @Override
    @Transactional
    public void softDeleteProductVariant(Long id) {
        ProductVariant variant = getProductVariantById(id);
        variant.setDeletedAt(LocalDateTime.now());
        variant.setDeletedBy(auditorAwareConfig.getCurrentAuditorReference());
        productVariantRepository.save(variant);
        log.info("Soft-deleted variant with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreProductVariant(Long id) {
        ProductVariant variant = productVariantRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
        variant.setDeletedAt(null);
        variant.setDeletedBy(null);
        productVariantRepository.save(variant);
        log.info("Restored variant with id: {}", id);
    }

    @Override
    public ProductVariant getProductVariantById(Long id) {
        return productVariantRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
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
                        .isFeatured(proj.getIsFeatured())
                        .isTrending(proj.getIsTrending())
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

    private PageResponse<ProductVariantResponse> getAllVariantsWithFilter(ProductVariantFilterRequest request, boolean isDeleted) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(),
                SortUtils.parseSort(request.sort()));

        Specification<ProductVariant> spec = ProductVariantSpecification.buildSpecification(request, isDeleted);
        Page<ProductVariantProjection> page = productVariantRepository.findBy(spec,
                q -> q.as(ProductVariantProjection.class).page(pageable));

        List<ProductVariantResponse> content = page.getContent().stream()
                .map(proj -> ProductVariantResponse.builder()
                        .id(proj.getId())
                        .product(proj.getProductName())
                        .sku(proj.getSku())
                        .name(proj.getName())
                        .price(proj.getPrice())
                        .oldPrice(proj.getOldPrice())
                        .image(proj.getImage())
                        .attributes(proj.getAttributes())
                        .status(proj.getStatus())
                        .createdAt(proj.getCreatedAt())
                        .createdBy(proj.getCreatedByFullName())
                        .updatedAt(proj.getUpdatedAt())
                        .updatedBy(proj.getUpdatedByFullName())
                        .deletedAt(proj.getDeletedAt())
                        .deletedBy(proj.getDeletedByFullName())
                        .build())
                .collect(Collectors.toList());

        if (!content.isEmpty()) {
            Set<Long> pvIds = content.stream().map(ProductVariantResponse::id).collect(Collectors.toSet());
            Map<Long, Integer> quantityMap = inventoryService.getQuantitiesByVariantIds(pvIds);
            
            Map<Long, List<VariantValueResponse>> valuesMap = productVariantValueRepository
                    .findAllByProductVariantIdInWithVariantValue(pvIds)
                    .stream()
                    .collect(Collectors.groupingBy(
                            pvv -> pvv.getProductVariant().getId(),
                            Collectors.mapping(pvv -> VariantValueResponse.builder()
                                    .id(pvv.getVariantValue().getId())
                                    .variantId(pvv.getVariantValue().getVariant().getId())
                                    .variantName(pvv.getVariantValue().getVariant().getName())
                                    .value(pvv.getVariantValue().getValue())
                                    .build(), Collectors.toList())));

            content = content.stream()
                    .map(pv -> pv.toBuilder()
                            .quantity(quantityMap.getOrDefault(pv.id(), 0))
                            .values(valuesMap.getOrDefault(pv.id(), List.of()))
                            .build())
                    .collect(Collectors.toList());
        }

        Page<ProductVariantResponse> responsePage = new PageImpl<>(content, pageable, page.getTotalElements());
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
        List<ProductCategoryResponse> categories = productCategoryRepository
                .findAllByProductIdInWithCategory(productIds)
                .stream()
                .map(pc -> new ProductCategoryResponse(
                        pc.getId(),
                        pc.getCategory().getId(),
                        pc.getCategory().getName()))
                .collect(Collectors.toList());

        // Load tags
        List<ProductTagResponse> tags = productTagRepository
                .findAllByProductId(product.getId())
                .stream()
                .map(pt -> new ProductTagResponse(
                        pt.getId(),
                        pt.getTag().getId(),
                        pt.getTag().getName(),
                        pt.getTag().getTagGroup().getName()))
                .collect(Collectors.toList());

        // Load images
        List<ProductImageResponse> images = productImageRepository.findAllByProductIdIn(productIds)
                .stream()
                .map(pi -> new ProductImageResponse(
                        pi.getId(),
                        pi.getImage(),
                        pi.getPosition()))
                .collect(Collectors.toList());

        return response.toBuilder()
                .categories(categories)
                .tags(tags)
                .images(images)
                .productVariants(loadProductVariants(List.of(product.getId())).getOrDefault(product.getId(), List.of()))
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

    private void saveProductTags(Product product, List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return;
        }

        List<Tag> tags = tagRepository.findAllById(tagIds);
        if (tags.size() != tagIds.size()) {
            throw new AppException(ErrorCode.TAG_NOT_FOUND);
        }

        List<ProductTag> productTags = tags.stream()
                .map(tag -> ProductTag.builder()
                        .product(product)
                        .tag(tag)
                        .build())
                .collect(Collectors.toList());

        productTagRepository.saveAll(productTags);
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

    private void mergeProductTags(Product product, List<Long> incomingTagIds) {
        Long productId = product.getId();

        if (incomingTagIds.isEmpty()) {
            productTagRepository.deleteAllByProductId(productId);
            return;
        }

        // Delete tags no longer in the request
        productTagRepository.deleteByProductIdAndTagIdNotIn(productId, incomingTagIds);

        // Find existing tagIds for this product
        List<Long> existingTagIds = productTagRepository.findTagIdsByProductId(productId);

        // Add only new tags
        List<Long> newTagIds = incomingTagIds.stream()
                .filter(id -> !existingTagIds.contains(id))
                .collect(Collectors.toList());

        if (!newTagIds.isEmpty()) {
            saveProductTags(product, newTagIds);
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
                .collect(Collectors.toList());

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

    private void saveProductVariants(Product product, List<ProductVariantRequest> productVariantRequests) {
        if (productVariantRequests == null || productVariantRequests.isEmpty()) {
            return;
        }

        // Pre-fetch all variant values to avoid query in loop
        Set<Long> allValueIds = productVariantRequests.stream()
                .flatMap(req -> req.variantValueIds().stream())
                .collect(Collectors.toSet());
        Map<Long, VariantValue> valueMap = variantValueRepository.findAllById(allValueIds)
                .stream().collect(Collectors.toMap(VariantValue::getId, vv -> vv));

        List<ProductVariant> productVariants = productVariantRequests.stream()
                .map(req -> ProductVariant.builder()
                        .product(product)
                        .sku(req.sku())
                        .name(req.name())
                        .price(req.price())
                        .oldPrice(req.oldPrice())
                        .image(req.image())
                        .attributes(req.attributes())
                        .status(req.status() != null ? req.status() : ProductVariantStatus.ACTIVE)
                        .build())
                .collect(Collectors.toList());

        List<ProductVariant> savedVariants = productVariantRepository.saveAll(productVariants);

        // Initialize inventory (Bulk)
        Map<ProductVariant, Integer> inventoryMap = IntStream.range(0, savedVariants.size())
                .boxed()
                .collect(Collectors.toMap(
                        savedVariants::get,
                        i -> productVariantRequests.get(i).quantity() != null ? productVariantRequests.get(i).quantity() : 0));
        inventoryService.initializeStocks(inventoryMap);

        // Save variant values (Bulk)
        List<ProductVariantValue> pvvToSave = IntStream.range(0, savedVariants.size())
                .boxed()
                .flatMap(i -> {
                    ProductVariant pv = savedVariants.get(i);
                    List<Long> valueIds = productVariantRequests.get(i).variantValueIds();
                    return valueIds.stream().map(vid -> {
                        VariantValue vv = valueMap.get(vid);
                        if (vv == null) throw new AppException(ErrorCode.VARIANT_VALUE_NOT_FOUND);
                        return ProductVariantValue.builder()
                                .productVariant(pv)
                                .variantValue(vv)
                                .build();
                    });
                })
                .collect(Collectors.toList());

        productVariantValueRepository.saveAll(pvvToSave);
    }

    private void saveProductVariantValues(ProductVariant variant, List<Long> variantValueIds) {
        if (variantValueIds == null || variantValueIds.isEmpty()) {
            return;
        }

        List<VariantValue> values = variantValueRepository.findAllById(variantValueIds);
        if (values.size() != variantValueIds.size()) {
            throw new AppException(ErrorCode.VARIANT_VALUE_NOT_FOUND);
        }

        List<ProductVariantValue> productVariantValues = values.stream()
                .map(vv -> ProductVariantValue.builder()
                        .productVariant(variant)
                        .variantValue(vv)
                        .build())
                .collect(Collectors.toList());

        productVariantValueRepository.saveAll(productVariantValues);
    }

    private void mergeProductVariants(Product product, List<ProductVariantRequest> incomingProductVariants) {
        Long productId = product.getId();

        if (incomingProductVariants == null || incomingProductVariants.isEmpty()) {
            Set<Long> allIds = productVariantRepository.findAllByProductIdAndDeletedAtIsNull(productId)
                    .stream().map(ProductVariant::getId).collect(Collectors.toSet());
            if (!allIds.isEmpty()) {
                productVariantRepository.softDeleteByIds(LocalDateTime.now(), auditorAwareConfig.getCurrentAuditorReference(), allIds);
            }
            return;
        }

        List<ProductVariantRequest> toUpdate = incomingProductVariants.stream()
                .filter(req -> req.id() != null)
                .toList();

        List<ProductVariantRequest> toInsert = incomingProductVariants.stream()
                .filter(req -> req.id() == null)
                .collect(Collectors.toList());

        Set<Long> idsToKeep = toUpdate.stream()
                .map(ProductVariantRequest::id)
                .collect(Collectors.toSet());

        productVariantRepository.softDeleteByProductIdAndIdNotIn(productId, idsToKeep, LocalDateTime.now(), auditorAwareConfig.getCurrentAuditorReference());

        if (!toUpdate.isEmpty()) {
            Map<Long, ProductVariant> existingMap = productVariantRepository.findAllById(idsToKeep)
                    .stream()
                    .collect(Collectors.toMap(ProductVariant::getId, pv -> pv));

            // Pre-fetch all value IDs for updates to optimize
            Set<Long> allValueIds = toUpdate.stream()
                    .flatMap(req -> req.variantValueIds().stream())
                    .collect(Collectors.toSet());
            Map<Long, VariantValue> valueMap = variantValueRepository.findAllById(allValueIds)
                    .stream().collect(Collectors.toMap(VariantValue::getId, vv -> vv));

            List<ProductVariantValue> allPvvToInsert = new ArrayList<>();

            for (ProductVariantRequest req : toUpdate) {
                ProductVariant existing = existingMap.get(req.id());
                if (existing != null) {
                    existing.setSku(req.sku());
                    existing.setName(req.name());
                    existing.setPrice(req.price());
                    existing.setOldPrice(req.oldPrice());
                    existing.setImage(req.image());
                    existing.setAttributes(req.attributes());
                    if (req.quantity() != null) {
                        inventoryService.updateStock(existing.getId(), req.quantity());
                    }
                    if (req.status() != null) {
                        existing.setStatus(req.status());
                    }
                    
                    productVariantValueRepository.deleteAllByProductVariantId(existing.getId());
                    List<ProductVariantValue> pvvList = req.variantValueIds().stream().map(vid -> {
                        VariantValue vv = valueMap.get(vid);
                        if (vv == null) throw new AppException(ErrorCode.VARIANT_VALUE_NOT_FOUND);
                        return ProductVariantValue.builder()
                                .productVariant(existing)
                                .variantValue(vv)
                                .build();
                    }).toList();
                    allPvvToInsert.addAll(pvvList);
                }
            }
            productVariantRepository.saveAll(existingMap.values());
            productVariantValueRepository.saveAll(allPvvToInsert);
        }

        if (!toInsert.isEmpty()) {
            saveProductVariants(product, toInsert);
        }
    }

    private Map<Long, List<ProductVariantResponse>> loadProductVariants(List<Long> productIds) {
        List<ProductVariant> productVariantsList = productVariantRepository.findAllByProductIdInAndDeletedAtIsNull(productIds);
        if (productVariantsList.isEmpty()) {
            return Collections.emptyMap();
        }

        Set<Long> variantIds = productVariantsList.stream().map(ProductVariant::getId).collect(Collectors.toSet());
        Map<Long, List<VariantValueResponse>> valuesMap = productVariantValueRepository
                .findAllByProductVariantIdInWithVariantValue(variantIds)
                .stream()
                .collect(Collectors.groupingBy(
                        pvv -> pvv.getProductVariant().getId(),
                        Collectors.mapping(pvv -> VariantValueResponse.builder()
                                .id(pvv.getVariantValue().getId())
                                .variantId(pvv.getVariantValue().getVariant().getId())
                                .variantName(pvv.getVariantValue().getVariant().getName())
                                .value(pvv.getVariantValue().getValue())
                                .build(), Collectors.toList())));

        Map<Long, Integer> quantityMap = inventoryService.getQuantitiesByVariantIds(variantIds);

        return productVariantsList.stream()
                .map(pv -> ProductVariantResponse.builder()
                        .id(pv.getId())
                        .productId(pv.getProduct().getId())
                        .product(pv.getProduct().getName())
                        .sku(pv.getSku())
                        .name(pv.getName())
                        .price(pv.getPrice())
                        .oldPrice(pv.getOldPrice())
                        .quantity(quantityMap.getOrDefault(pv.getId(), 0))
                        .status(pv.getStatus())
                        .image(pv.getImage())
                        .attributes(pv.getAttributes())
                        .values(valuesMap.getOrDefault(pv.getId(), Collections.emptyList()))
                        .build())
                .collect(Collectors.groupingBy(ProductVariantResponse::productId));
    }

}
