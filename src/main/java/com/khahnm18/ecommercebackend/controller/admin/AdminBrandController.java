package com.khahnm18.ecommercebackend.controller.admin;

import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.product.BrandRequest;
import com.khahnm18.ecommercebackend.dto.request.product.BrandSearchRequest;
import com.khahnm18.ecommercebackend.dto.response.ApiResponse;
import com.khahnm18.ecommercebackend.dto.response.PageResponse;
import com.khahnm18.ecommercebackend.dto.response.product.BrandResponse;
import com.khahnm18.ecommercebackend.service.product.BrandService;
import com.khahnm18.ecommercebackend.util.Translator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.UUID;

@Slf4j(topic = "ADMIN-BRAND-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/admin/brands")
@RequiredArgsConstructor
@Tag(name = "Admin Brand Controller", description = "Admin Brand Management APIs")
public class AdminBrandController {

    private final BrandService brandService;
    private final Translator translator;

    @GetMapping("/{id}")
    @Operation(summary = "Get brand detail by id")
    @PreAuthorize("hasAuthority('BRAND_VIEW')")
    ApiResponse<BrandResponse> getBrandDetailById(
        @PathVariable("id") UUID brandId,
        @RequestParam(defaultValue = "false") boolean isDeleted
    ) {
        log.info("Get brand detail: brandId={}", brandId);
        return ApiResponse.<BrandResponse>builder()
            .status(HttpStatus.OK.value())
            .data(brandService.getBrandDetailById(brandId, isDeleted))
            .message(translator.toLocale("brand.detail.success"))
            .build();
    }

    @GetMapping
    @Operation(summary = "Get all and search brands (support pagination)")
    @PreAuthorize("hasAuthority('BRAND_VIEW')")
    PageResponse<BrandResponse> getAllAndSearchBrands(
        @RequestParam(required = false, defaultValue = "1") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        BrandSearchRequest request
    ) {
        log.info("Get all and search brands");
        Page<BrandResponse> brandPage = brandService.getAllAndSearchBrands(page, size, request);
        return PageResponse.<BrandResponse>builder()
            .status(HttpStatus.OK.value())
            .data(brandPage.getContent())
            .meta(PageResponse.Meta.fromPage(brandPage))
            .message(translator.toLocale("brand.list.success"))
            .build();
    }

    @PostMapping
    @Operation(summary = "Create brand")
    @PreAuthorize("hasAuthority('BRAND_CREATE')")
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<BrandResponse> createBrand(
        @Valid @RequestBody BrandRequest request
    ) {
        log.info("Create brand");
        return ApiResponse.<BrandResponse>builder()
            .status(HttpStatus.CREATED.value())
            .data(brandService.createBrand(request))
            .message(translator.toLocale("brand.create.success"))
            .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update brand")
    @PreAuthorize("hasAuthority('BRAND_UPDATE')")
    ApiResponse<BrandResponse> updateBrand(
        @PathVariable("id") UUID brandId,
        @Valid @RequestBody BrandRequest request
    ) {
        log.info("Update brand: brandId={}", brandId);
        return ApiResponse.<BrandResponse>builder()
            .status(HttpStatus.OK.value())
            .data(brandService.updateBrand(brandId, request))
            .message(translator.toLocale("brand.update.success"))
            .build();
    }

    @PatchMapping("/{id}/soft-delete")
    @Operation(summary = "Soft delete brand")
    @PreAuthorize("hasAuthority('BRAND_TRASH')")
    ApiResponse<Void> softDeleteBrand(
        @PathVariable("id") UUID brandId
    ) {
        log.info("Soft delete brand: brandId={}", brandId);
        brandService.softDeleteBrand(brandId);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("brand.soft-delete.success"))
            .build();
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Restore brand")
    @PreAuthorize("hasAuthority('BRAND_TRASH')")
    ApiResponse<Void> restoreBrand(
        @PathVariable("id") UUID brandId
    ) {
        log.info("Restore brand: brandId={}", brandId);
        brandService.restoreBrand(brandId);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("brand.restore.success"))
            .build();
    }

    @PatchMapping("/bulk-action")
    @Operation(summary = "Bulk action brand")
    @PreAuthorize("hasAnyAuthority('BRAND_UPDATE', 'BRAND_TRASH', 'BRAND_DELETE')")
    ApiResponse<Void> bulkAction(
        @Valid @RequestBody BulkActionRequest request
    ) {
        brandService.bulkAction(request);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("brand.bulk-action.success"))
            .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete brand")
    @PreAuthorize("hasAuthority('BRAND_DELETE')")
    ApiResponse<Void> deleteBrand(
        @PathVariable("id") UUID brandId
    ) {
        log.info("Delete brand: brandId={}", brandId);
        brandService.deleteBrand(brandId);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("brand.delete.success"))
            .build();
    }

}
