package com.khahnm04.ecommerce.modules.product.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.khahnm04.ecommerce.common.base.dto.ApiResponse;
import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.modules.product.dto.request.VariantFilterRequest;
import com.khahnm04.ecommerce.modules.product.dto.request.VariantRequest;
import com.khahnm04.ecommerce.modules.product.dto.response.VariantResponse;
import com.khahnm04.ecommerce.modules.product.service.VariantService;
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
@RequestMapping("${api.prefix}/admin/variants")
@RequiredArgsConstructor
public class AdminVariantController {

    private final VariantService variantService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<VariantResponse> createVariant (
            @Valid @RequestBody VariantRequest request
    ) {
        return ApiResponse.<VariantResponse>builder()
                .data(variantService.createVariant(request))
                .message("Variant created successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<VariantResponse> getVariantDetail (
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean deleted
    ) {
        return ApiResponse.<VariantResponse>builder()
                .data(variantService.getVariantDetail(id, deleted))
                .message("Get variant detail successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<VariantResponse>> getAllVariants (
            @Valid @ModelAttribute VariantFilterRequest filter
    ) {
        PageResponse<VariantResponse> pageResponse = variantService.getAllVariants(filter);
        return ApiResponse.<List<VariantResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get all variants successfully")
                .build();
    }

    @GetMapping("/deleted")
    public ApiResponse<List<VariantResponse>> getAllDeletedVariants (
            @Valid @ModelAttribute VariantFilterRequest filter
    ) {
        PageResponse<VariantResponse> pageResponse = variantService.getAllDeletedVariants(filter);
        return ApiResponse.<List<VariantResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get deleted variants successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<VariantResponse> updateVariant (
            @PathVariable Long id,
            @Valid @RequestBody VariantRequest request
    ) {
        return ApiResponse.<VariantResponse>builder()
                .data(variantService.updateVariant(id, request))
                .message("Variant updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDeleteVariant (
            @PathVariable Long id
    ) {
        variantService.softDeleteVariant(id);
        return ApiResponse.<Void>builder()
                .message("Variant soft deleted successfully")
                .build();
    }

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restoreVariant (
            @PathVariable Long id
    ) {
        variantService.restoreVariant(id);
        return ApiResponse.<Void>builder()
                .message("Variant restored successfully")
                .build();
    }

    @PatchMapping("/bulk")
    public ApiResponse<Void> handleBulkAction (
            @RequestBody JsonNode request
    ) {
        JsonNode variantIdsNode = request.get("variantIds");
        if (variantIdsNode == null || !variantIdsNode.isArray()) {
            throw new AppException(ErrorCode.VARIANT_SELECTION_REQUIRED);
        }
        Set<Long> variantIds = new HashSet<>();
        variantIdsNode.forEach(node -> variantIds.add(node.asLong()));
        if (variantIds.isEmpty()) {
            throw new AppException(ErrorCode.VARIANT_SELECTION_REQUIRED);
        }

        JsonNode actionNode = request.get("action");
        if (actionNode == null || actionNode.asText().isBlank()) {
            throw new AppException(ErrorCode.VARIANT_ACTION_REQUIRED);
        }
        String action = actionNode.asText();

        variantService.handleBulkAction(variantIds, action);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
