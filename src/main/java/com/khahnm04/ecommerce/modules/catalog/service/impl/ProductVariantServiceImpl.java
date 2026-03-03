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

        String sku = StringUtils.hasText(request.getSku())
                ? request.getSku().trim()
                : generateSku(product);

        if (productVariantRepository.existsBySku(sku)) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_EXISTED);
        }

        ProductVariant productVariant = productVariantMapper.toProductVariant(request);
        productVariant.setProduct(product);
        productVariant.setSku(sku);

        ProductVariant saved = productVariantRepository.save(productVariant);

        if (request.getVariantValueIds() != null && !request.getVariantValueIds().isEmpty()) {
            saveProductVariantValues(saved, request.getVariantValueIds());
        }

        log.info("Created product variant id: {} with sku: {}", saved.getId(), sku);
        return buildResponse(saved);
    }

    @Override
    public PageResponse<ProductVariantResponse> getAllProductVariants(ProductVariantFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<ProductVariantResponse> page = this.getAllWithFilterAndSearch(request, pageable, false);
        return PageResponse.fromPage(page);
    }

    @Override
    public PageResponse<ProductVariantResponse> getAllDeletedProductVariants(ProductVariantFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<ProductVariantResponse> page = this.getAllWithFilterAndSearch(request, pageable, true);
        return PageResponse.fromPage(page);
    }

    @Override
    public ProductVariant getProductVariantById(Long id) {
        return productVariantRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
    }

    @Override
    public ProductVariantResponse getProductVariantDetailById(Long id) {
        ProductVariant productVariant = getProductVariantById(id);
        return buildResponse(productVariant);
    }

    @Override
    public ProductVariantResponse getProductVariantDetailBySku(String sku) {
        ProductVariant productVariant = productVariantRepository.findBySku(sku)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
        return buildResponse(productVariant);
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

        if (request.getVariantValueIds() != null) {
            productVariantValueRepository.deleteAllByProductVariantId(id);
            if (!request.getVariantValueIds().isEmpty()) {
                saveProductVariantValues(saved, request.getVariantValueIds());
            }
        }

        log.info("Updated product variant id: {}", saved.getId());
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
            return;
        }
        productVariant.setDeletedAt(LocalDateTime.now());
        productVariantRepository.save(productVariant);
        log.info("Soft deleted product variant id {}", id);
    }

    @Override
    @Transactional
    public void restoreProductVariant(Long id) {
        productVariantRepository.findDeletedProductVariantById(id)
                .ifPresentOrElse(productVariant -> {
                    if (productVariant.getDeletedAt() == null) {
                        log.warn("Product variant with id {} is not deleted, no restoration needed", id);
                        return;
                    }
                    productVariant.setDeletedAt(null);
                    productVariantRepository.save(productVariant);
                    log.info("Restored product variant id {}", id);
                }, () -> {
                    throw new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND);
                });
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

    /**
     * Generate SKU automatically: {PRODUCT_CODE}-{TIMESTAMP}
     * Example: IP15PM-20260220161044
     */
    private String generateSku(Product product) {
        String prefix = StringUtils.hasText(product.getProductCode())
                ? product.getProductCode().toUpperCase()
                : "PV";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return prefix + "-" + timestamp;
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

    private Page<ProductVariantResponse> getAllWithFilterAndSearch(
            ProductVariantFilterRequest request, Pageable pageable, boolean isDeleted) {
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

        return new PageImpl<>(content, pageable, total);
    }

}
