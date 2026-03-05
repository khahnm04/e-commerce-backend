package com.khahnm04.ecommerce.modules.catalog.controller;

import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.catalog.dto.request.BrandBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.BrandFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.BrandRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.BrandResponse;
import com.khahnm04.ecommerce.modules.catalog.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/brands")
@RequiredArgsConstructor
public class AdminBrandController {

    private final BrandService brandService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<BrandResponse> createBrand(
            @Valid @RequestBody BrandRequest request
    ) {
        return ApiResponse.<BrandResponse>builder()
                .data(brandService.createBrand(request))
                .message("Brand created successfully")
                .build();
    }

    @GetMapping("/{identifier}")
    public ApiResponse<BrandResponse> getBrandDetail(
            @PathVariable String identifier,
            @RequestParam(defaultValue = "false") boolean deleted
    ) {
        return ApiResponse.<BrandResponse>builder()
                .data(brandService.getBrandDetail(identifier, deleted))
                .message("Get brand detail successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<BrandResponse>> getAllBrands(
            @Valid @ModelAttribute BrandFilterRequest filter
    ) {
        PageResponse<BrandResponse> pageResponse = brandService.getAllBrands(filter);
        return ApiResponse.<List<BrandResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get all brands successfully")
                .build();
    }

    @GetMapping("/deleted")
    public ApiResponse<List<BrandResponse>> getAllDeletedBrands(
            @Valid @ModelAttribute BrandFilterRequest filter
    ) {
        PageResponse<BrandResponse> pageResponse = brandService.getAllDeletedBrands(filter);
        return ApiResponse.<List<BrandResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get deleted brands successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<BrandResponse> updateBrand(
            @PathVariable Long id,
            @Valid @RequestBody BrandRequest request
    ) {
        return ApiResponse.<BrandResponse>builder()
                .data(brandService.updateBrand(id, request))
                .message("Brand updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDeleteBrand(
            @PathVariable Long id
    ) {
        brandService.softDeleteBrand(id);
        return ApiResponse.<Void>builder()
                .message("Brand soft deleted successfully")
                .build();
    }

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restoreBrand(
            @PathVariable Long id
    ) {
        brandService.restoreBrand(id);
        return ApiResponse.<Void>builder()
                .message("Brand restored successfully")
                .build();
    }

    @PatchMapping("/bulk")
    public ApiResponse<Void> handleBulkAction(
            @Valid @RequestBody BrandBulkActionRequest request
    ) {
        brandService.handleBulkAction(request);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
