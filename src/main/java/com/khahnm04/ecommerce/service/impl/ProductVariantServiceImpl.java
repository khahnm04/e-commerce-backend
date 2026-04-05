package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.common.enums.ProductVariantStatus;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.ProductVariantFilterRequest;
import com.khahnm04.ecommerce.dto.request.ProductVariantRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.ProductVariantResponse;
import com.khahnm04.ecommerce.entity.Product;
import com.khahnm04.ecommerce.entity.ProductVariant;
import com.khahnm04.ecommerce.entity.ProductVariantValue;
import com.khahnm04.ecommerce.entity.VariantValue;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.ProductVariantMapper;
import com.khahnm04.ecommerce.repository.ProductVariantRepository;
import com.khahnm04.ecommerce.repository.ProductVariantValueRepository;
import com.khahnm04.ecommerce.repository.VariantValueRepository;
import com.khahnm04.ecommerce.service.InventoryService;
import com.khahnm04.ecommerce.service.ProductService;
import com.khahnm04.ecommerce.service.ProductVariantService;
import com.khahnm04.ecommerce.specification.ProductVariantSpecification;
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
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductVariantValueRepository productVariantValueRepository;
    private final VariantValueRepository variantValueRepository;
    private final ProductVariantMapper productVariantMapper;
    private final ProductService productService;
    private final InventoryService inventoryService;

    @Override
    @Transactional
    public ProductVariantResponse createProductVariant(ProductVariantRequest request) {
        Product product = productService.getProductById(request.productId());

        String sku;
        if (StringUtils.hasText(request.sku())) {
            sku = request.sku().trim();
            if (productVariantRepository.existsBySku(sku)) {
                throw new AppException(ErrorCode.PRODUCT_VARIANT_EXISTED);
            }
        } else {
            sku = generateSku(product, request.variantValues());
        }

        ProductVariant productVariant = productVariantMapper.toProductVariant(request);
        productVariant.setProduct(product);
        productVariant.setSku(sku);
        productVariant.setStatus(request.status() == null ? ProductVariantStatus.ACTIVE : request.status());

        ProductVariant saved = productVariantRepository.save(productVariant);

        if (request.variantValues() != null && !request.variantValues().isEmpty()) {
            saveProductVariantValues(saved, request.variantValues());
        }

        // Initialize inventory for the new variant
        inventoryService.initializeStock(saved, request.quantity() != null ? request.quantity() : 0);

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

        if (request.productId() != null) {
            Product product = productService.getProductById(request.productId());
            productVariant.setProduct(product);
        }

        if (StringUtils.hasText(request.sku())
                && productVariantRepository.existsBySkuAndIdNot(request.sku(), id)) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_EXISTED);
        }

        productVariantMapper.updateProductVariant(productVariant, request);

        if (StringUtils.hasText(request.sku())) {
            productVariant.setSku(request.sku().trim());
        }

        ProductVariant saved = productVariantRepository.save(productVariant);

        if (request.variantValues() != null) {
            productVariantValueRepository.deleteAllByProductVariantId(id);
            if (!request.variantValues().isEmpty()) {
                saveProductVariantValues(saved, request.variantValues());
            }
        }

        if (request.quantity() != null) {
            inventoryService.updateStock(id, request.quantity());
        }

        log.info("Updated product variant with id: {}", saved.getId());
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
    public void handleBulkAction(BulkActionRequest request) {
        Set<Long> productVariantIds = request.ids();
        String action = request.action().toUpperCase();

        if (productVariantIds == null || productVariantIds.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_BULK_ACTION_REQUIRED_IDS);
        }

        switch (action) {
            case "ACTIVE", "INACTIVE", "OUT_OF_STOCK" -> {
                ProductVariantStatus status = ProductVariantStatus.valueOf(action);
                productVariantRepository.updateStatusByIds(status, productVariantIds);
                log.info("Bulk update status {} product variants ids: {}", action, productVariantIds);
            }
            case "DELETE" -> {
                productVariantRepository.softDeleteByIds(LocalDateTime.now(), productVariantIds);
                log.info("Bulk delete product variants ids: {}", productVariantIds);
            }
            case "RESTORE" -> {
                productVariantRepository.restoreDeletedByIds(productVariantIds);
                log.info("Bulk restore product variants ids: {}", productVariantIds);
            }
            default -> throw new AppException(ErrorCode.PRODUCT_VARIANT_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} product variants", action, productVariantIds.size());
    }

    // ======================== Private methods ========================

    private ProductVariant getDeletedProductVariantById(Long id) {
        return productVariantRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
    }

    private String generateSku(Product product, Set<Long> variantValueIds) {
        String prefix = StringUtils.hasText(product.getCode())
                ? product.getCode().toUpperCase()
                : "PV";

        StringBuilder skuBuilder = new StringBuilder(prefix);

        if (variantValueIds != null && !variantValueIds.isEmpty()) {
            List<VariantValue> variantValues = variantValueRepository.findAllById(variantValueIds);

            String variantsPart = variantValues.stream()
                    .sorted(Comparator.comparing(vv -> vv.getVariant().getName()))
                    .map(vv -> vv.getValue().toUpperCase().replaceAll("\\s+", ""))
                    .collect(Collectors.joining("-"));

            if (StringUtils.hasText(variantsPart)) {
                skuBuilder.append("-").append(variantsPart);
            }
        }

        String shortUuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        skuBuilder.append("-").append(shortUuid);

        String sku = skuBuilder.toString();

        int counter = 1;
        String baseSku = sku;
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

        List<ProductVariantValue> pvValues = productVariantValueRepository
                .findAllByProductVariantIdWithVariantValue(productVariant.getId());

        List<ProductVariantResponse.VariantValueInfo> variantValueInfos = pvValues.stream()
                .map(pvv -> ProductVariantResponse.VariantValueInfo.builder()
                        .id(pvv.getVariantValue().getId())
                        .variant(pvv.getVariantValue().getVariant().getName())
                        .value(pvv.getVariantValue().getValue())
                        .build())
                .toList();

        Integer quantity = inventoryService.getQuantityByVariantId(productVariant.getId());

        return response.toBuilder()
                .quantity(quantity)
                .variantValues(variantValueInfos)
                .build();
    }

    private PageResponse<ProductVariantResponse> getAllWithFilterAndSearch(ProductVariantFilterRequest request,
            boolean isDeleted) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(),
                SortUtils.parseSort(request.sort()));

        Specification<ProductVariant> spec = ProductVariantSpecification.buildSpecification(request, isDeleted);
        Page<ProductVariant> page = productVariantRepository.findAll(spec, pageable);

        List<ProductVariantResponse> content = page.getContent().stream()
                .map(productVariantMapper::toProductVariantResponse)
                .collect(Collectors.toList());

        if (!content.isEmpty()) {
            Set<Long> pvIds = content.stream()
                    .map(ProductVariantResponse::id)
                    .collect(Collectors.toSet());

            List<ProductVariantValue> allPvValues = productVariantValueRepository
                    .findAllByProductVariantIdInWithVariantValue(pvIds);

            Map<Long, Integer> quantityMap = inventoryService.getQuantitiesByVariantIds(pvIds);

            Map<Long, List<ProductVariantResponse.VariantValueInfo>> grouped = allPvValues.stream()
                    .collect(Collectors.groupingBy(
                            pvv -> pvv.getProductVariant().getId(),
                            Collectors.mapping(
                                    pvv -> ProductVariantResponse.VariantValueInfo.builder()
                                            .id(pvv.getVariantValue().getId())
                                            .variant(pvv.getVariantValue().getVariant().getName())
                                            .value(pvv.getVariantValue().getValue())
                                            .build(),
                                    Collectors.toList())));

            content = content.stream()
                    .map(pv -> pv.toBuilder()
                            .quantity(quantityMap.getOrDefault(pv.id(), 0))
                            .variantValues(grouped.getOrDefault(pv.id(), List.of()))
                            .build())
                    .collect(Collectors.toList());
        }

        Page<ProductVariantResponse> responsePage = new PageImpl<>(content, pageable, page.getTotalElements());
        return PageResponse.fromPage(responsePage);
    }

}
