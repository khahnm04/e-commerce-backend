package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.config.Translator;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.brand.BrandFilterRequest;
import com.khahnm04.ecommerce.dto.request.brand.BrandRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.brand.BrandResponse;
import com.khahnm04.ecommerce.service.brand.BrandService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "BRAND-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/admin/brands")
@RequiredArgsConstructor
@Tag(name = "Admin Brand Controller", description = "APIs for managing brands")
public class AdminBrandController {

    private final BrandService brandService;
    private final Translator translator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create brand")
    @PreAuthorize("hasAuthority('BRAND_CREATE')")
    ApiResponse<BrandResponse> createBrand(
        @Valid @RequestBody BrandRequest request
    ) {
        log.info("Create brand");
        return ApiResponse.<BrandResponse>builder()
            .data(brandService.createBrand(request))
            .message(translator.toLocale("BRAND_CREATED_SUCCESS"))
            .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get brand detail")
    @PreAuthorize("hasAuthority('BRAND_VIEW')")
    ApiResponse<BrandResponse> getBrandDetailById(
        @PathVariable Long id,
        @RequestParam(defaultValue = "false") boolean isDeleted
    ) {
        log.info("Get brand detail");
        return ApiResponse.<BrandResponse>builder()
            .data(brandService.getBrandDetailById(id, isDeleted))
            .message(translator.toLocale("BRAND_GET_DETAIL_SUCCESS"))
            .build();
    }

    @GetMapping
    @Operation(summary = "Get all brands")
    @PreAuthorize("hasAuthority('BRAND_VIEW')")
    PageResponse<BrandResponse> getAllBrands(
        @Valid @ModelAttribute BrandFilterRequest request
    ) {
        log.info("Get all brands");
        Page<BrandResponse> page = brandService.getAllBrands(request);

        return PageResponse.<BrandResponse>builder()
            .data(page.getContent())
            .meta(PageResponse.Meta.fromPage(page))
            .message(translator.toLocale("BRAND_GET_ALL_SUCCESS"))
            .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update brand")
    @PreAuthorize("hasAuthority('BRAND_UPDATE')")
    ApiResponse<BrandResponse> updateBrand(
        @PathVariable Long id,
        @Valid @RequestBody BrandRequest request
    ) {
        log.info("Update brand");
        return ApiResponse.<BrandResponse>builder()
            .data(brandService.updateBrand(id, request))
            .message(translator.toLocale("BRAND_UPDATED_SUCCESS"))
            .build();
    }

    @PatchMapping("/{id}/soft-delete")
    @Operation(summary = "Soft delete brand")
    @PreAuthorize("hasAuthority('BRAND_DELETE')")
    ApiResponse<Void> softDeleteBrand(
        @PathVariable Long id
    ) {
        log.info("Soft delete brand");
        brandService.softDeleteBrand(id);

        return ApiResponse.<Void>builder()
            .message(translator.toLocale("BRAND_SOFT_DELETED_SUCCESS"))
            .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete brand")
    @PreAuthorize("hasAuthority('BRAND_DELETE')")
    ApiResponse<Void> deleteBrand(
        @PathVariable Long id
    ) {
        log.info("Delete brand");
        brandService.deleteBrand(id);

        return ApiResponse.<Void>builder()
            .message(translator.toLocale("BRAND_DELETED_SUCCESS"))
            .build();
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Restore brand")
    @PreAuthorize("hasAuthority('BRAND_DELETE')")
    ApiResponse<Void> restoreBrand(
        @PathVariable Long id
    ) {
        log.info("Restore brand");
        brandService.restoreBrand(id);

        return ApiResponse.<Void>builder()
            .message(translator.toLocale("BRAND_RESTORED_SUCCESS"))
            .build();
    }

    @PatchMapping("/bulk")
    @Operation(summary = "Bulk action brands")
    @PreAuthorize("hasAnyAuthority('BRAND_UPDATE', 'BRAND_DELETE')")
    ApiResponse<Void> bulkAction(
        @RequestBody BulkActionRequest request
    ) {
        brandService.bulkAction(request);

        return ApiResponse.<Void>builder()
            .message(translator.toLocale("BRAND_BULK_ACTION_SUCCESS"))
            .build();
    }

}
