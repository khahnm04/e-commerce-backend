package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.BrandFilterRequest;
import com.khahnm04.ecommerce.dto.request.BrandRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.BrandResponse;
import com.khahnm04.ecommerce.service.BrandService;
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
            @Valid @RequestBody BrandRequest request) {
        return ApiResponse.<BrandResponse>builder()
                .data(brandService.createBrand(request))
                .message("Brand created successfully")
                .build();
    }

    @GetMapping("/{identifier}")
    public ApiResponse<BrandResponse> getBrandDetail(
            @PathVariable String identifier,
            @RequestParam(defaultValue = "false") boolean deleted) {
        return ApiResponse.<BrandResponse>builder()
                .data(brandService.getBrandDetail(identifier, deleted))
                .message("Get brand detail successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<BrandResponse>> getAllBrands(
            @Valid @ModelAttribute BrandFilterRequest filter) {
        PageResponse<BrandResponse> pageResponse = brandService.getAllBrands(filter);
        return ApiResponse.<List<BrandResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get all brands successfully")
                .build();
    }

    @GetMapping("/deleted")
    public ApiResponse<List<BrandResponse>> getAllDeletedBrands(
            @Valid @ModelAttribute BrandFilterRequest filter) {
        PageResponse<BrandResponse> pageResponse = brandService.getAllDeletedBrands(filter);
        return ApiResponse.<List<BrandResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get deleted brands successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<BrandResponse> updateBrand(
            @PathVariable Long id,
            @Valid @RequestBody BrandRequest request) {
        return ApiResponse.<BrandResponse>builder()
                .data(brandService.updateBrand(id, request))
                .message("Brand updated successfully")
                .build();
    }

    @PatchMapping("/{id}/soft-delete")
    public ApiResponse<Void> softDeleteBrand(
            @PathVariable Long id) {
        brandService.softDeleteBrand(id);
        return ApiResponse.<Void>builder()
                .message("Brand soft deleted successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBrand(
            @PathVariable Long id) {
        brandService.deleteBrand(id);
        return ApiResponse.<Void>builder()
                .message("Brand deleted successfully")
                .build();
    }

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restoreBrand(
            @PathVariable Long id) {
        brandService.restoreBrand(id);
        return ApiResponse.<Void>builder()
                .message("Brand restored successfully")
                .build();
    }

    @PatchMapping("/bulk")
    public ApiResponse<Void> handleBulkAction(
            @RequestBody BulkActionRequest request) {
        brandService.handleBulkAction(request);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
