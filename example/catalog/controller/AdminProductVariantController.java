package com.khahnm04.ecommerce.modules.catalog.controller;

import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductVariantBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductVariantFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductVariantRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductVariantUpdateStatusRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.ProductVariantResponse;
import com.khahnm04.ecommerce.modules.catalog.service.ProductVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get all product variants successfully")
                .build();
    }

    @GetMapping("/deleted")
    public ApiResponse<List<ProductVariantResponse>> getAllDeletedProductVariants(
            @Valid @ModelAttribute ProductVariantFilterRequest filter
    ) {
        PageResponse<ProductVariantResponse> pageResponse = productVariantService
                .getAllDeletedProductVariants(filter);
        return ApiResponse.<List<ProductVariantResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
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
    public ApiResponse<ProductVariantResponse> updateProductVariantStatus(
            @PathVariable Long id,
            @Valid @RequestBody ProductVariantUpdateStatusRequest request
    ) {
        return ApiResponse.<ProductVariantResponse>builder()
                .data(productVariantService.updateProductVariantStatus(id, request))
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
            @Valid @RequestBody ProductVariantBulkActionRequest request
    ) {
        productVariantService.handleBulkAction(request);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
