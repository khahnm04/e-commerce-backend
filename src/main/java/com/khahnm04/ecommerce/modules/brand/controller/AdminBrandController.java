package com.khahnm04.ecommerce.modules.brand.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.khahnm04.ecommerce.common.base.dto.ApiResponse;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.modules.brand.dto.request.BrandFilterRequest;
import com.khahnm04.ecommerce.modules.brand.dto.request.BrandRequest;
import com.khahnm04.ecommerce.modules.brand.dto.response.BrandResponse;
import com.khahnm04.ecommerce.modules.brand.enums.BrandStatus;
import com.khahnm04.ecommerce.modules.brand.service.BrandService;
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
@RequestMapping("${api.prefix}/admin/brands")
@RequiredArgsConstructor
public class AdminBrandController {

    private final BrandService brandService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<BrandResponse> createBrand (
            @Valid @RequestBody BrandRequest request
    ) {
        return ApiResponse.<BrandResponse>builder()
                .data(brandService.createBrand(request))
                .message("Brand created successfully")
                .build();
    }

    @GetMapping("/{identifier}")
    public ApiResponse<BrandResponse> getBrandDetail (
            @PathVariable String identifier,
            @RequestParam(defaultValue = "false") boolean deleted
    ) {
        return ApiResponse.<BrandResponse>builder()
                .data(brandService.getBrandDetail(identifier, deleted))
                .message("Get brand detail successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<BrandResponse>> getAllBrands (
            @Valid @ModelAttribute BrandFilterRequest filter
    ) {
        PageResponse<BrandResponse> pageResponse = brandService.getAllBrands(filter);
        return ApiResponse.<List<BrandResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get all brands successfully")
                .build();
    }

    @GetMapping("/deleted")
    public ApiResponse<List<BrandResponse>> getAllDeletedBrands (
            @Valid @ModelAttribute BrandFilterRequest filter
    ) {
        PageResponse<BrandResponse> pageResponse = brandService.getAllDeletedBrands(filter);
        return ApiResponse.<List<BrandResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get deleted brands successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<BrandResponse> updateBrand (
            @PathVariable Long id,
            @Valid @RequestBody BrandRequest request
    ) {
        return ApiResponse.<BrandResponse>builder()
                .data(brandService.updateBrand(id, request))
                .message("Brand updated successfully")
                .build();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateBrandStatus (
            @PathVariable Long id,
            @RequestBody JsonNode request
    ) {
        JsonNode statusNode = request.get("status");
        if (statusNode == null || statusNode.isNull()) {
            throw new AppException(ErrorCode.BRAND_STATUS_REQUIRED);
        }
        BrandStatus status;
        try {
            status = BrandStatus.valueOf(statusNode.asText().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.BRAND_STATUS_INVALID);
        }
        brandService.updateBrandStatus(id, status);
        return ApiResponse.<Void>builder()
                .message("Brand status updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDeleteBrand (
            @PathVariable Long id
    ) {
        brandService.softDeleteBrand(id);
        return ApiResponse.<Void>builder()
                .message("Brand soft deleted successfully")
                .build();
    }

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restoreBrand (
            @PathVariable Long id
    ) {
        brandService.restoreBrand(id);
        return ApiResponse.<Void>builder()
                .message("Brand restored successfully")
                .build();
    }

    @PatchMapping("/bulk")
    public ApiResponse<Void> handleBulkAction (
            @RequestBody JsonNode request
    ) {
        JsonNode brandIdsNode = request.get("brandIds");
        if (brandIdsNode == null || !brandIdsNode.isArray()) {
            throw new AppException(ErrorCode.BRAND_SELECTION_REQUIRED);
        }
        Set<Long> brandIds = new HashSet<>();
        brandIdsNode.forEach(node -> brandIds.add(node.asLong()));
        if (brandIds.isEmpty()) {
            throw new AppException(ErrorCode.BRAND_SELECTION_REQUIRED);
        }

        JsonNode actionNode = request.get("action");
        if (actionNode == null || actionNode.asText().isBlank()) {
            throw new AppException(ErrorCode.BRAND_ACTION_REQUIRED);
        }
        String action = actionNode.asText();

        brandService.handleBulkAction(brandIds, action);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
