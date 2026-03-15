package com.khahnm04.ecommerce.modules.product.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.khahnm04.ecommerce.common.base.dto.ApiResponse;
import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.modules.product.dto.request.ProductVariantFilterRequest;
import com.khahnm04.ecommerce.modules.product.dto.request.ProductVariantRequest;
import com.khahnm04.ecommerce.modules.product.dto.response.ProductVariantResponse;
import com.khahnm04.ecommerce.modules.product.enums.ProductVariantStatus;
import com.khahnm04.ecommerce.modules.product.service.ProductVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/product-variants")
@RequiredArgsConstructor
public class AdminProductVariantController {

    private final ProductVariantService productVariantService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProductVariantResponse> createProductVariant(
            @Valid @RequestBody ProductVariantRequest request
    ) {
        return ApiResponse.<ProductVariantResponse>builder()
                .data(productVariantService.createProductVariant(request))
                .message("Product variant created successfully")
                .build();
    }

    @GetMapping("/{identifier}")
    public ApiResponse<ProductVariantResponse> getProductVariantDetail(
            @PathVariable String identifier,
            @RequestParam(defaultValue = "false") boolean deleted
    ) {
        return ApiResponse.<ProductVariantResponse>builder()
                .data(productVariantService.getProductVariantDetail(identifier, deleted))
                .message("Get product variant detail successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<ProductVariantResponse>> getAllProductVariants(
            @Valid @ModelAttribute ProductVariantFilterRequest filter
    ) {
        PageResponse<ProductVariantResponse> pageResponse = productVariantService.getAllProductVariants(filter);
        return ApiResponse.<List<ProductVariantResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get all product variants successfully")
                .build();
    }

    @GetMapping("/deleted")
    public ApiResponse<List<ProductVariantResponse>> getAllDeletedProductVariants(
            @Valid @ModelAttribute ProductVariantFilterRequest filter
    ) {
        PageResponse<ProductVariantResponse> pageResponse = productVariantService.getAllDeletedProductVariants(filter);
        return ApiResponse.<List<ProductVariantResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get deleted product variants successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductVariantResponse> updateProductVariant(
            @PathVariable Long id,
            @Valid @RequestBody ProductVariantRequest request
    ) {
        return ApiResponse.<ProductVariantResponse>builder()
                .data(productVariantService.updateProductVariant(id, request))
                .message("Product variant updated successfully")
                .build();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateProductVariantStatus(
            @PathVariable Long id,
            @RequestBody JsonNode request
    ) {
        JsonNode statusNode = request.get("status");
        if (statusNode == null || statusNode.isNull()) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_STATUS_REQUIRED);
        }
        ProductVariantStatus status;
        try {
            status = ProductVariantStatus.valueOf(statusNode.asText().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_INVALID_BULK_ACTION);
        }
        productVariantService.updateProductVariantStatus(id, status);
        return ApiResponse.<Void>builder()
                .message("Product variant status updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDeleteProductVariant(
            @PathVariable Long id
    ) {
        productVariantService.softDeleteProductVariant(id);
        return ApiResponse.<Void>builder()
                .message("Product variant soft deleted successfully")
                .build();
    }

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restoreProductVariant(
            @PathVariable Long id
    ) {
        productVariantService.restoreProductVariant(id);
        return ApiResponse.<Void>builder()
                .message("Product variant restored successfully")
                .build();
    }

    @PatchMapping("/bulk")
    public ApiResponse<Void> handleBulkAction(
            @RequestBody JsonNode request
    ) {
        JsonNode productVariantIdsNode = request.get("productVariantIds");
        if (productVariantIdsNode == null || !productVariantIdsNode.isArray()) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_SELECTION_REQUIRED);
        }
        Set<Long> productVariantIds = new HashSet<>();
        productVariantIdsNode.forEach(node -> productVariantIds.add(node.asLong()));
        if (productVariantIds.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_SELECTION_REQUIRED);
        }

        JsonNode actionNode = request.get("action");
        if (actionNode == null || actionNode.asText().isBlank()) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_ACTION_REQUIRED);
        }
        String action = actionNode.asText();

        productVariantService.handleBulkAction(productVariantIds, action);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
