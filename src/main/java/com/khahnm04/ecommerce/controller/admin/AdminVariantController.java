package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.VariantFilterRequest;
import com.khahnm04.ecommerce.dto.request.VariantRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.VariantResponse;
import com.khahnm04.ecommerce.service.VariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
            @RequestBody BulkActionRequest request
    ) {
        variantService.handleBulkAction(request);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
