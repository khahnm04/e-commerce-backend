package com.khahnm04.ecommerce.modules.catalog.controller;

import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.catalog.dto.request.VariantBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.VariantFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.VariantRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.VariantResponse;
import com.khahnm04.ecommerce.modules.catalog.service.VariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/admin/variants")
@RequiredArgsConstructor
public class AdminVariantController {

    private final VariantService variantService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<VariantResponse> createVariant(
            @Valid @RequestBody VariantRequest request
    ) {
        return ApiResponse.<VariantResponse>builder()
                .data(variantService.createVariant(request))
                .message("Variant created successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<VariantResponse>> getAllVariants(
            @Valid @ModelAttribute VariantFilterRequest filter
    ) {
        PageResponse<VariantResponse> pageResponse = variantService.getAllVariants(filter);
        return ApiResponse.<List<VariantResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get all variants successfully")
                .build();
    }

    @GetMapping("/deleted")
    public ApiResponse<List<VariantResponse>> getAllDeletedVariants(
            @Valid @ModelAttribute VariantFilterRequest filter) {
        PageResponse<VariantResponse> pageResponse = variantService.getAllDeletedVariants(filter);
        return ApiResponse.<List<VariantResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get deleted variants successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<VariantResponse> getVariantDetailById(
            @PathVariable Long id) {
        return ApiResponse.<VariantResponse>builder()
                .data(variantService.getVariantDetailById(id))
                .message("Get variant detail successfully")
                .build();
    }

    @GetMapping("/code/{code}")
    public ApiResponse<VariantResponse> getVariantDetailByCode(
            @PathVariable String code) {
        return ApiResponse.<VariantResponse>builder()
                .data(variantService.getVariantDetailByCode(code))
                .message("Get variant detail by code successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<VariantResponse> updateVariant(
            @PathVariable Long id,
            @Valid @RequestBody VariantRequest request) {
        return ApiResponse.<VariantResponse>builder()
                .data(variantService.updateVariant(id, request))
                .message("Variant updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDeleteVariant(
            @PathVariable Long id) {
        variantService.softDeleteVariant(id);
        return ApiResponse.<Void>builder()
                .message("Variant soft deleted successfully")
                .build();
    }

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restoreVariant(
            @PathVariable Long id) {
        variantService.restoreVariant(id);
        return ApiResponse.<Void>builder()
                .message("Variant restored successfully")
                .build();
    }

    @PatchMapping("/bulk")
    public ApiResponse<Void> handleBulkAction(
            @Valid @RequestBody VariantBulkActionRequest request) {
        variantService.handleBulkAction(request);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
