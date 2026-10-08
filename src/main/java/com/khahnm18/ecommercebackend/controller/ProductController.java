package com.khahnm18.ecommercebackend.controller;

import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.product.ProductRequest;
import com.khahnm18.ecommercebackend.dto.response.ApiResponse;
import com.khahnm18.ecommercebackend.dto.response.PageResponse;
import com.khahnm18.ecommercebackend.dto.response.product.ProductResponse;
import com.khahnm18.ecommercebackend.enums.ProductStatus;
import com.khahnm18.ecommercebackend.service.product.ProductService;
import com.khahnm18.ecommercebackend.util.Translator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;
import java.util.UUID;

@Slf4j(topic = "PRODUCT-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "Product management APIs")
public class ProductController {

    private final Translator translator;
    private final ProductService productService;

    @GetMapping("/{id}")
    @Operation(summary = "Get product detail by id")
    @PreAuthorize("hasAuthority('PRODUCT_VIEW')")
    ApiResponse<ProductResponse> getProductDetailById(
        @PathVariable("id") UUID productId,
        @RequestParam(defaultValue = "false") boolean isDeleted
    ) {
        log.info("Get product detail: productId={}", productId);
        return ApiResponse.<ProductResponse>builder()
            .status(HttpStatus.OK.value())
            .data(productService.getProductDetailById(productId, isDeleted))
            .message(translator.toLocale("product.detail.success"))
            .build();
    }

    @GetMapping
    @Operation(summary = "Get all products")
    @PreAuthorize("hasAuthority('PRODUCT_VIEW')")
    PageResponse<ProductResponse> getAllProducts(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "false") boolean isDeleted,
        @RequestParam(defaultValue = "createdAt,desc") String sort,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Instant startDate,
        @RequestParam(required = false) Instant endDate,
        @RequestParam(required = false) ProductStatus status,
        @RequestParam(required = false) UUID versionId
    ) {
        log.info("Get all products");
        Page<ProductResponse> productPage = productService.getAllProducts(
            page, size, sort, keyword, isDeleted, startDate, endDate, status, versionId
        );
        return PageResponse.<ProductResponse>builder()
            .status(HttpStatus.OK.value())
            .data(productPage.getContent())
            .meta(PageResponse.Meta.fromPage(productPage))
            .message(translator.toLocale("product.list.success"))
            .build();
    }

    @PostMapping
    @Operation(summary = "Create product")
    @PreAuthorize("hasAuthority('PRODUCT_CREATE')")
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        log.info("Create product");
        return ApiResponse.<ProductResponse>builder()
            .status(HttpStatus.CREATED.value())
            .data(productService.createProduct(request))
            .message(translator.toLocale("product.create.success"))
            .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product")
    @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
    ApiResponse<ProductResponse> updateProduct(
        @PathVariable("id") UUID productId,
        @Valid @RequestBody ProductRequest request
    ) {
        log.info("Update product: productId={}", productId);
        return ApiResponse.<ProductResponse>builder()
            .status(HttpStatus.OK.value())
            .data(productService.updateProduct(productId, request))
            .message(translator.toLocale("product.update.success"))
            .build();
    }

    @PatchMapping("/{id}/soft-delete")
    @Operation(summary = "Soft delete product")
    @PreAuthorize("hasAuthority('PRODUCT_TRASH')")
    ApiResponse<Void> softDeleteProduct(@PathVariable("id") UUID productId) {
        log.info("Soft delete product: productId={}", productId);
        productService.softDeleteProduct(productId);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("product.soft-delete.success"))
            .build();
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Restore product")
    @PreAuthorize("hasAuthority('PRODUCT_TRASH')")
    ApiResponse<Void> restoreProduct(@PathVariable("id") UUID productId) {
        log.info("Restore product: productId={}", productId);
        productService.restoreProduct(productId);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("product.restore.success"))
            .build();
    }

    @PatchMapping("/bulk-action")
    @Operation(summary = "Bulk action products")
    @PreAuthorize("hasAnyAuthority('PRODUCT_UPDATE', 'PRODUCT_TRASH', 'PRODUCT_DELETE')")
    ApiResponse<Void> bulkAction(@Valid @RequestBody BulkActionRequest request) {
        productService.bulkAction(request);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("product.bulk-action.success"))
            .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product")
    @PreAuthorize("hasAuthority('PRODUCT_DELETE')")
    ApiResponse<Void> deleteProduct(@PathVariable("id") UUID productId) {
        log.info("Delete product: productId={}", productId);
        productService.deleteProduct(productId);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("product.delete.success"))
            .build();
    }
}
