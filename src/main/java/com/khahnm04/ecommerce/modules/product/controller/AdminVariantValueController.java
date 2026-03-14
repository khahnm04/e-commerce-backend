package com.khahnm04.ecommerce.modules.product.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.khahnm04.ecommerce.common.base.dto.ApiResponse;
import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.modules.product.dto.request.VariantValueFilterRequest;
import com.khahnm04.ecommerce.modules.product.dto.request.VariantValueRequest;
import com.khahnm04.ecommerce.modules.product.dto.response.VariantValueResponse;
import com.khahnm04.ecommerce.modules.product.service.VariantValueService;
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
@RequestMapping("${api.prefix}/admin/variant-values")
@RequiredArgsConstructor
public class AdminVariantValueController {

    private final VariantValueService variantValueService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<VariantValueResponse> createVariantValue (
            @Valid @RequestBody VariantValueRequest request
    ) {
        return ApiResponse.<VariantValueResponse>builder()
                .data(variantValueService.createVariantValue(request))
                .message("Variant value created successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<VariantValueResponse> getVariantValueDetail (
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean deleted
    ) {
        return ApiResponse.<VariantValueResponse>builder()
                .data(variantValueService.getVariantValueDetail(id, deleted))
                .message("Get variant value detail successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<VariantValueResponse>> getAllVariantValues (
            @Valid @ModelAttribute VariantValueFilterRequest filter
    ) {
        PageResponse<VariantValueResponse> pageResponse = variantValueService.getAllVariantValues(filter);
        return ApiResponse.<List<VariantValueResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get all variant values successfully")
                .build();
    }

    @GetMapping("/deleted")
    public ApiResponse<List<VariantValueResponse>> getAllDeletedVariantValues (
            @Valid @ModelAttribute VariantValueFilterRequest filter
    ) {
        PageResponse<VariantValueResponse> pageResponse = variantValueService.getAllDeletedVariantValues(filter);
        return ApiResponse.<List<VariantValueResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get deleted variant values successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<VariantValueResponse> updateVariantValue (
            @PathVariable Long id,
            @Valid @RequestBody VariantValueRequest request
    ) {
        return ApiResponse.<VariantValueResponse>builder()
                .data(variantValueService.updateVariantValue(id, request))
                .message("Variant value updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDeleteVariantValue (
            @PathVariable Long id
    ) {
        variantValueService.softDeleteVariantValue(id);
        return ApiResponse.<Void>builder()
                .message("Variant value soft deleted successfully")
                .build();
    }

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restoreVariantValue (
            @PathVariable Long id
    ) {
        variantValueService.restoreVariantValue(id);
        return ApiResponse.<Void>builder()
                .message("Variant value restored successfully")
                .build();
    }

    @PatchMapping("/bulk")
    public ApiResponse<Void> handleBulkAction (
            @RequestBody JsonNode request
    ) {
        JsonNode variantValueIdsNode = request.get("variantValueIds");
        if (variantValueIdsNode == null || !variantValueIdsNode.isArray()) {
            throw new AppException(ErrorCode.VARIANT_VALUE_SELECTION_REQUIRED);
        }
        Set<Long> variantValueIds = new HashSet<>();
        variantValueIdsNode.forEach(node -> variantValueIds.add(node.asLong()));
        if (variantValueIds.isEmpty()) {
            throw new AppException(ErrorCode.VARIANT_VALUE_SELECTION_REQUIRED);
        }

        JsonNode actionNode = request.get("action");
        if (actionNode == null || actionNode.asText().isBlank()) {
            throw new AppException(ErrorCode.VARIANT_VALUE_ACTION_REQUIRED);
        }
        String action = actionNode.asText();

        variantValueService.handleBulkAction(variantValueIds, action);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
