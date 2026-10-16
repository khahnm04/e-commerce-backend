package com.khahnm18.ecommercebackend.controller.admin;

import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.product.ProductVersionRequest;
import com.khahnm18.ecommercebackend.dto.response.ApiResponse;
import com.khahnm18.ecommercebackend.dto.response.PageResponse;
import com.khahnm18.ecommercebackend.dto.response.product.ProductVersionResponse;
import com.khahnm18.ecommercebackend.service.product.ProductVersionService;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.util.UUID;

@Slf4j(topic = "PRODUCT-VERSION-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/product-versions")
@RequiredArgsConstructor
@Tag(name = "Product Version Controller", description = "Product version management APIs")
public class AdminProductVersionController {

    private final Translator translator;
    private final ProductVersionService productVersionService;

    @GetMapping("/{id}")
    @Operation(summary = "Get product version detail by id")
    @PreAuthorize("hasAuthority('PRODUCT_VERSION_VIEW')")
    ApiResponse<ProductVersionResponse> getProductVersionDetailById(
        @PathVariable("id") UUID productVersionId,
        @RequestParam(defaultValue = "false") boolean isDeleted
    ) {
        log.info("Get product version detail: productVersionId={}", productVersionId);
        return ApiResponse.<ProductVersionResponse>builder()
            .status(HttpStatus.OK.value())
            .data(productVersionService.getProductVersionDetailById(productVersionId, isDeleted))
            .message(translator.toLocale("product-version.detail.success"))
            .build();
    }

    @GetMapping
    @Operation(summary = "Get all product versions")
    @PreAuthorize("hasAuthority('PRODUCT_VERSION_VIEW')")
    PageResponse<ProductVersionResponse> getAllProductVersions(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "false") boolean isDeleted,
        @RequestParam(defaultValue = "createdAt,desc") String sort,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Instant startDate,
        @RequestParam(required = false) Instant endDate,
        @RequestParam(required = false) UUID groupId
    ) {
        log.info("Get all product versions");
        Page<ProductVersionResponse> productVersionPage = productVersionService.getAllProductVersions(
            page,
            size,
            sort,
            keyword,
            isDeleted,
            startDate,
            endDate,
            groupId
        );
        return PageResponse.<ProductVersionResponse>builder()
            .status(HttpStatus.OK.value())
            .data(productVersionPage.getContent())
            .meta(PageResponse.Meta.fromPage(productVersionPage))
            .message(translator.toLocale("product-version.list.success"))
            .build();
    }

    @PostMapping
    @Operation(summary = "Create product version")
    @PreAuthorize("hasAuthority('PRODUCT_VERSION_CREATE')")
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<ProductVersionResponse> createProductVersion(
        @Valid @RequestBody ProductVersionRequest request
    ) {
        log.info("Create product version");
        return ApiResponse.<ProductVersionResponse>builder()
            .status(HttpStatus.CREATED.value())
            .data(productVersionService.createProductVersion(request))
            .message(translator.toLocale("product-version.create.success"))
            .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product version")
    @PreAuthorize("hasAuthority('PRODUCT_VERSION_UPDATE')")
    ApiResponse<ProductVersionResponse> updateProductVersion(
        @PathVariable("id") UUID productVersionId,
        @Valid @RequestBody ProductVersionRequest request
    ) {
        log.info("Update product version: productVersionId={}", productVersionId);
        return ApiResponse.<ProductVersionResponse>builder()
            .status(HttpStatus.OK.value())
            .data(productVersionService.updateProductVersion(productVersionId, request))
            .message(translator.toLocale("product-version.update.success"))
            .build();
    }

    @PatchMapping("/{id}/soft-delete")
    @Operation(summary = "Soft delete product version")
    @PreAuthorize("hasAuthority('PRODUCT_VERSION_TRASH')")
    ApiResponse<Void> softDeleteProductVersion(
        @PathVariable("id") UUID productVersionId
    ) {
        log.info("Soft delete product version: productVersionId={}", productVersionId);
        productVersionService.softDeleteProductVersion(productVersionId);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("product-version.soft-delete.success"))
            .build();
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Restore product version")
    @PreAuthorize("hasAuthority('PRODUCT_VERSION_TRASH')")
    ApiResponse<Void> restoreProductVersion(
        @PathVariable("id") UUID productVersionId
    ) {
        log.info("Restore product version: productVersionId={}", productVersionId);
        productVersionService.restoreProductVersion(productVersionId);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("product-version.restore.success"))
            .build();
    }

    @PatchMapping("/bulk-action")
    @Operation(summary = "Bulk action product versions")
    @PreAuthorize("hasAnyAuthority('PRODUCT_VERSION_UPDATE', 'PRODUCT_VERSION_TRASH', 'PRODUCT_VERSION_DELETE')")
    ApiResponse<Void> bulkAction(
        @Valid @RequestBody BulkActionRequest request
    ) {
        productVersionService.bulkAction(request);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("product-version.bulk-action.success"))
            .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product version")
    @PreAuthorize("hasAuthority('PRODUCT_VERSION_DELETE')")
    ApiResponse<Void> deleteProductVersion(
        @PathVariable("id") UUID productVersionId
    ) {
        log.info("Delete product version: productVersionId={}", productVersionId);
        productVersionService.deleteProductVersion(productVersionId);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("product-version.delete.success"))
            .build();
    }

}
