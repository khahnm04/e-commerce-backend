package com.khahnm04.ecommerce.modules.catalog.service.impl;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.common.entity.BaseEntity;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductVariantBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductVariantFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductVariantRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductVariantUpdateStatusRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.ProductVariantResponse;
import com.khahnm04.ecommerce.modules.catalog.entity.Product;
import com.khahnm04.ecommerce.modules.catalog.entity.ProductVariant;
import com.khahnm04.ecommerce.modules.catalog.entity.ProductVariantValue;
import com.khahnm04.ecommerce.modules.catalog.entity.VariantValue;
import com.khahnm04.ecommerce.modules.catalog.enums.ProductVariantSortField;
import com.khahnm04.ecommerce.modules.catalog.enums.ProductVariantStatus;
import com.khahnm04.ecommerce.modules.catalog.mapper.ProductVariantMapper;
import com.khahnm04.ecommerce.modules.catalog.mapper.mybaits.ProductVariantMyBatisMapper;
import com.khahnm04.ecommerce.modules.catalog.repository.ProductVariantRepository;
import com.khahnm04.ecommerce.modules.catalog.repository.ProductVariantValueRepository;
import com.khahnm04.ecommerce.modules.catalog.repository.VariantValueRepository;
import com.khahnm04.ecommerce.modules.catalog.service.ProductService;
import com.khahnm04.ecommerce.modules.catalog.service.ProductVariantService;
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
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductVariantValueRepository productVariantValueRepository;
    private final VariantValueRepository variantValueRepository;
    private final ProductVariantMapper productVariantMapper;
    private final ProductVariantMyBatisMapper productVariantMyBatisMapper;
    private final ProductService productService;

    @Override
    @Transactional
    public ProductVariantResponse createProductVariant(ProductVariantRequest request) {
        Product product = productService.getProductById(request.getProductId());

        String sku;
        if (StringUtils.hasText(request.getSku())) {
            sku = request.getSku().trim();
            if (productVariantRepository.existsBySku(sku)) {
                throw new AppException(ErrorCode.PRODUCT_VARIANT_EXISTED);
            }
        } else {
            sku = generateSku(product, request.getVariantValues());
        }

        ProductVariant productVariant = productVariantMapper.toProductVariant(request);
        productVariant.setProduct(product);
        productVariant.setSku(sku);
        productVariant.setStatus(request.getStatus() == null ? ProductVariantStatus.ACTIVE : request.getStatus());

        ProductVariant saved = productVariantRepository.save(productVariant);

        if (request.getVariantValues() != null && !request.getVariantValues().isEmpty()) {
            saveProductVariantValues(saved, request.getVariantValues());
        }

        log.info("Created product variant with id: {} and sku: {}", saved.getId(), sku);
        return buildResponse(saved);
    }

    @Override
    public ProductVariant getProductVariantById(Long id) {
        return productVariantRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
    }

    @Override
    public ProductVariantResponse getProductVariantDetail(String identifier, boolean deleted) {
        ProductVariant productVariant;

        if (identifier.matches("\\d+")) {
            Long id = Long.parseLong(identifier);
            productVariant = deleted
                    ? getDeletedProductVariantById(id)
                    : getProductVariantById(id);
        } else {
            productVariant = deleted
                    ? productVariantRepository.findBySkuAndDeletedAtIsNotNull(identifier)
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND))
                    : productVariantRepository.findBySkuAndDeletedAtIsNull(identifier)
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
        }

        return buildResponse(productVariant);
    }

    @Override
    public PageResponse<ProductVariantResponse> getAllProductVariants(ProductVariantFilterRequest request) {
        return getAllWithFilterAndSearch(request, false);
    }

    @Override
    public PageResponse<ProductVariantResponse> getAllDeletedProductVariants(ProductVariantFilterRequest request) {
        return getAllWithFilterAndSearch(request, true);
    }

    @Override
    @Transactional
    public ProductVariantResponse updateProductVariant(Long id, ProductVariantRequest request) {
        ProductVariant productVariant = getProductVariantById(id);

        if (request.getProductId() != null) {
            Product product = productService.getProductById(request.getProductId());
            productVariant.setProduct(product);
        }

        if (StringUtils.hasText(request.getSku())
                && productVariantRepository.existsBySkuAndIdNot(request.getSku(), id)) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_EXISTED);
        }

        productVariantMapper.updateProductVariant(productVariant, request);

        if (StringUtils.hasText(request.getSku())) {
            productVariant.setSku(request.getSku().trim());
        }

        ProductVariant saved = productVariantRepository.save(productVariant);

        if (request.getVariantValues() != null) {
            productVariantValueRepository.deleteAllByProductVariantId(id);
            if (!request.getVariantValues().isEmpty()) {
                saveProductVariantValues(saved, request.getVariantValues());
            }
        }

        log.info("Updated product variant with id: {}", saved.getId());
        return buildResponse(saved);
    }

    @Override
    @Transactional
    public ProductVariantResponse updateProductVariantStatus(Long id, ProductVariantUpdateStatusRequest request) {
        ProductVariant productVariant = getProductVariantById(id);
        productVariant.setStatus(request.getStatus());
        ProductVariant saved = productVariantRepository.save(productVariant);
        log.info("Updated product variant id: {} status to: {}", saved.getId(), request.getStatus());
        return buildResponse(saved);
    }

    @Override
    @Transactional
    public void softDeleteProductVariant(Long id) {
        ProductVariant productVariant = getProductVariantById(id);
        if (productVariant.getDeletedAt() != null) {
            log.warn("Product variant with id {} is already soft deleted", id);
            return;
        }
        productVariant.setDeletedAt(LocalDateTime.now());
        productVariantRepository.save(productVariant);
        log.info("Soft-deleted product variant with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreProductVariant(Long id) {
        ProductVariant productVariant = getDeletedProductVariantById(id);
        if (productVariant.getDeletedAt() == null) {
            log.warn("Product variant with id {} is not in a deleted state", id);
            return;
        }
        productVariant.setDeletedAt(null);
        productVariantRepository.save(productVariant);
        log.info("Product variant restored with id {}", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(ProductVariantBulkActionRequest request) {
        Set<Long> productVariantIds = request.getProductVariants();
        if (productVariantIds == null || productVariantIds.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_BULK_ACTION_REQUIRED_IDS);
        }

        String action = request.getAction().toUpperCase();

        Set<ProductVariant> existingProductVariants = new HashSet<>(
                action.equals("RESTORE")
                        ? productVariantRepository.findAllDeletedByIds(productVariantIds)
                        : productVariantRepository.findAllById(productVariantIds));

        Set<Long> existingIds = existingProductVariants.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        if (productVariantIds.size() != existingIds.size()) {
            Set<Long> missingIds = new HashSet<>(productVariantIds);
            missingIds.removeAll(existingIds);
            log.error("Bulk action product variants failed. Missing Ids: {}", missingIds);
            throw new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND);
        }

        switch (action) {
            case "ACTIVE", "INACTIVE", "OUT_OF_STOCK" -> {
                ProductVariantStatus status = ProductVariantStatus.valueOf(action);
                productVariantRepository.updateStatusByIds(status, existingIds);
                log.info("Bulk update status {} product variants ids: {}", action, existingIds);
            }
            case "DELETE" -> {
                productVariantRepository.softDeleteByIds(LocalDateTime.now(), existingIds);
                log.info("Bulk delete product variants ids: {}", existingIds);
            }
            case "RESTORE" -> {
                productVariantRepository.restoreDeletedByIds(existingIds);
                log.info("Bulk restore product variants ids: {}", existingIds);
            }
            default -> throw new AppException(ErrorCode.PRODUCT_VARIANT_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} product variants", action, existingIds.size());
    }

    // ======================== Private methods ========================

    private ProductVariant getDeletedProductVariantById(Long id) {
        return productVariantRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
    }

    /**
     * Generate SKU from product code and variant values.
     * Format: {PRODUCT_CODE}-{VARIANT_VALUE_1}-{VARIANT_VALUE_2}-...
     * Variant values are sorted by variant name for consistency.
     * Falls back to timestamp suffix if no variant values are provided.
     * Appends counter (-1, -2, ...) if SKU already exists.
     *
     * Examples:
     * IP15PM-RED-256GB (Color=Red, Storage=256GB)
     * IP15PM-BLK-512GB (Color=Black, Storage=512GB)
     * IP15PM-20260220161044 (no variant values)
     */
    private String generateSku(Product product, Set<Long> variantValueIds) {
        String prefix = StringUtils.hasText(product.getCode())
                ? product.getCode().toUpperCase()
                : "PV";

        String baseSku;
        if (variantValueIds != null && !variantValueIds.isEmpty()) {
            List<VariantValue> variantValues = variantValueRepository.findAllById(variantValueIds);

            String suffix = variantValues.stream()
                    .sorted(Comparator.comparing(vv -> vv.getVariant().getName()))
                    .map(vv -> StringUtils.hasText(vv.getCode())
                            ? vv.getCode().toUpperCase()
                            : vv.getValue().toUpperCase().replaceAll("\\s+", ""))
                    .collect(Collectors.joining("-"));

            baseSku = prefix + "-" + suffix;
        } else {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            baseSku = prefix + "-" + timestamp;
        }

        String sku = baseSku;
        int counter = 1;
        while (productVariantRepository.existsBySku(sku)) {
            sku = baseSku + "-" + counter;
            counter++;
        }
        return sku;
    }

    private void saveProductVariantValues(ProductVariant productVariant, Set<Long> variantValueIds) {
        List<VariantValue> variantValues = variantValueRepository.findAllById(variantValueIds);
        if (variantValues.size() != variantValueIds.size()) {
            throw new AppException(ErrorCode.VARIANT_VALUE_NOT_FOUND);
        }

        List<ProductVariantValue> pvValues = variantValues.stream()
                .map(vv -> ProductVariantValue.builder()
                        .productVariant(productVariant)
                        .variantValue(vv)
                        .build())
                .toList();

        productVariantValueRepository.saveAll(pvValues);
    }

    private ProductVariantResponse buildResponse(ProductVariant productVariant) {
        ProductVariantResponse response = productVariantMapper.toProductVariantResponse(productVariant);

        // Load variant values with JOIN FETCH to avoid N+1
        List<ProductVariantValue> pvValues = productVariantValueRepository
                .findAllByProductVariantIdWithVariantValue(productVariant.getId());

        List<ProductVariantResponse.VariantValueInfo> variantValueInfos = pvValues.stream()
                .map(pvv -> ProductVariantResponse.VariantValueInfo.builder()
                        .id(pvv.getVariantValue().getId())
                        .variantName(pvv.getVariantValue().getVariant().getName())
                        .value(pvv.getVariantValue().getValue())
                        .code(pvv.getVariantValue().getCode())
                        .build())
                .toList();

        response.setVariantValues(variantValueInfos);
        return response;
    }

    private PageResponse<ProductVariantResponse> getAllWithFilterAndSearch(
            ProductVariantFilterRequest request, boolean isDeleted) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim()
                : null;

        String[] sortParams = SortUtils.parseSortForMyBatis(request.getSort(), ProductVariantSortField.class);
        String sortColumn = sortParams[0];
        String sortDirection = sortParams[1];

        List<ProductVariantResponse> content = productVariantMyBatisMapper.findAllWithFilterAndSearch(
                request.getProductId(),
                keyword,
                request.getStartDate(),
                request.getEndDate(),
                isDeleted,
                sortColumn,
                sortDirection,
                pageable.getOffset(),
                pageable.getPageSize());

        if (!content.isEmpty()) {
            Set<Long> pvIds = content.stream()
                    .map(ProductVariantResponse::getId)
                    .collect(Collectors.toSet());

            List<ProductVariantValue> allPvValues = productVariantValueRepository
                    .findAllByProductVariantIdInWithVariantValue(pvIds);

            Map<Long, List<ProductVariantResponse.VariantValueInfo>> grouped = allPvValues.stream()
                    .collect(Collectors.groupingBy(
                            pvv -> pvv.getProductVariant().getId(),
                            Collectors.mapping(
                                    pvv -> ProductVariantResponse.VariantValueInfo.builder()
                                            .id(pvv.getVariantValue().getId())
                                            .variantName(pvv.getVariantValue().getVariant().getName())
                                            .value(pvv.getVariantValue().getValue())
                                            .code(pvv.getVariantValue().getCode())
                                            .build(),
                                    Collectors.toList())));

            content.forEach(pv -> pv.setVariantValues(grouped.getOrDefault(pv.getId(), List.of())));
        }

        long total = productVariantMyBatisMapper.countWithFilterAndSearch(
                request.getProductId(),
                keyword,
                request.getStartDate(),
                request.getEndDate(),
                isDeleted);

        Page<ProductVariantResponse> page = new PageImpl<>(content, pageable, total);
        return PageResponse.fromPage(page);
    }

}
