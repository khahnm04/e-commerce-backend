package com.khahnm04.ecommerce.modules.catalog.controller;

import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.catalog.dto.request.VariantValueBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.VariantValueFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.VariantValueRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.VariantValueResponse;
import com.khahnm04.ecommerce.modules.catalog.service.VariantValueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/admin/variant-values")
@RequiredArgsConstructor
public class AdminVariantValueController {

    private final VariantValueService variantValueService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<VariantValueResponse> createVariantValue(
            @Valid @RequestBody VariantValueRequest request
    ) {
        return ApiResponse.<VariantValueResponse>builder()
                .data(variantValueService.createVariantValue(request))
                .message("Variant value created successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<VariantValueResponse>> getAllVariantValues(
            @Valid @ModelAttribute VariantValueFilterRequest filter
    ) {
        PageResponse<VariantValueResponse> pageResponse = variantValueService.getAllVariantValues(filter);
        return ApiResponse.<List<VariantValueResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get all variant values successfully")
                .build();
    }

    @GetMapping("/deleted")
    public ApiResponse<List<VariantValueResponse>> getAllDeletedVariantValues(
            @Valid @ModelAttribute VariantValueFilterRequest filter
    ) {
        PageResponse<VariantValueResponse> pageResponse = variantValueService
                .getAllDeletedVariantValues(filter);
        return ApiResponse.<List<VariantValueResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get deleted variant values successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<VariantValueResponse> getVariantValueDetailById(
            @PathVariable Long id
    ) {
        return ApiResponse.<VariantValueResponse>builder()
                .data(variantValueService.getVariantValueDetailById(id))
                .message("Get variant value detail successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<VariantValueResponse> updateVariantValue(
            @PathVariable Long id,
            @Valid @RequestBody VariantValueRequest request
    ) {
        return ApiResponse.<VariantValueResponse>builder()
                .data(variantValueService.updateVariantValue(id, request))
                .message("Variant value updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDeleteVariantValue(
            @PathVariable Long id
    ) {
        variantValueService.softDeleteVariantValue(id);
        return ApiResponse.<Void>builder()
                .message("Variant value soft deleted successfully")
                .build();
    }

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restoreVariantValue(
            @PathVariable Long id
    ) {
        variantValueService.restoreVariantValue(id);
        return ApiResponse.<Void>builder()
                .message("Variant value restored successfully")
                .build();
    }

    @PatchMapping("/bulk")
    public ApiResponse<Void> handleBulkAction(
            @Valid @RequestBody VariantValueBulkActionRequest request
    ) {
        variantValueService.handleBulkAction(request);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
