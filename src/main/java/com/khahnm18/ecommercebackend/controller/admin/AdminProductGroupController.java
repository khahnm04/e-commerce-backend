package com.khahnm18.ecommercebackend.controller.admin;

import com.khahnm18.ecommercebackend.util.Translator;
import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.product.ProductGroupRequest;
import com.khahnm18.ecommercebackend.dto.request.product.ProductGroupSearchRequest;
import com.khahnm18.ecommercebackend.dto.response.ApiResponse;
import com.khahnm18.ecommercebackend.dto.response.PageResponse;
import com.khahnm18.ecommercebackend.dto.response.product.ProductGroupResponse;
import com.khahnm18.ecommercebackend.service.product.ProductGroupService;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.util.UUID;

@Slf4j(topic = "ADMIN-PRODUCT-GROUP-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/admin/product-groups")
@RequiredArgsConstructor
@Tag(name = "Admin Product Group Controller", description = "Admin Product Group Management APIs")
public class AdminProductGroupController {

    private final Translator translator;
    private final ProductGroupService productGroupService;

    @GetMapping("/{id}")
    @Operation(summary = "Get product group detail by id")
    @PreAuthorize("hasAuthority('PRODUCT_GROUP_VIEW')")
    ApiResponse<ProductGroupResponse> getProductGroupDetailById(
        @PathVariable("id") UUID productGroupId,
        @RequestParam(defaultValue = "false") boolean isDeleted
    ) {
        log.info("Get product group detail: productGroupId={}", productGroupId);
        return ApiResponse.<ProductGroupResponse>builder()
            .status(HttpStatus.OK.value())
            .data(productGroupService.getProductGroupDetailById(productGroupId, isDeleted))
            .message(translator.toLocale("product-group.detail.success"))
            .build();
    }

    @GetMapping
    @Operation(summary = "Get all product groups")
    @PreAuthorize("hasAuthority('PRODUCT_GROUP_VIEW')")
    PageResponse<ProductGroupResponse> getAllProductGroups(
        @RequestParam(required = false, defaultValue = "1") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        ProductGroupSearchRequest request
    ) {
        log.info("Get all product groups");
        Page<ProductGroupResponse> productGroupPage = productGroupService.getAllProductGroups(
            page,
            size,
            request
        );
        return PageResponse.<ProductGroupResponse>builder()
            .status(HttpStatus.OK.value())
            .data(productGroupPage.getContent())
            .meta(PageResponse.Meta.fromPage(productGroupPage))
            .message(translator.toLocale("product-group.list.success"))
            .build();
    }

    @PostMapping
    @Operation(summary = "Create product group")
    @PreAuthorize("hasAuthority('PRODUCT_GROUP_CREATE')")
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<ProductGroupResponse> createProductGroup(
        @Valid @RequestBody ProductGroupRequest request
    ) {
        log.info("Create product group");
        return ApiResponse.<ProductGroupResponse>builder()
            .status(HttpStatus.CREATED.value())
            .data(productGroupService.createProductGroup(request))
            .message(translator.toLocale("product-group.create.success"))
            .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product group")
    @PreAuthorize("hasAuthority('PRODUCT_GROUP_UPDATE')")
    ApiResponse<ProductGroupResponse> updateProductGroup(
        @PathVariable("id") UUID productGroupId,
        @Valid @RequestBody ProductGroupRequest request
    ) {
        log.info("Update product group: productGroupId={}", productGroupId);
        return ApiResponse.<ProductGroupResponse>builder()
            .status(HttpStatus.OK.value())
            .data(productGroupService.updateProductGroup(productGroupId, request))
            .message(translator.toLocale("product-group.update.success"))
            .build();
    }

    @PatchMapping("/{id}/soft-delete")
    @Operation(summary = "Soft delete product group")
    @PreAuthorize("hasAuthority('PRODUCT_GROUP_TRASH')")
    ApiResponse<Void> softDeleteProductGroup(
        @PathVariable("id") UUID productGroupId
    ) {
        log.info("Soft delete product group: productGroupId={}", productGroupId);
        productGroupService.softDeleteProductGroup(productGroupId);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("product-group.soft-delete.success"))
            .build();
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Restore product group")
    @PreAuthorize("hasAuthority('PRODUCT_GROUP_TRASH')")
    ApiResponse<Void> restoreProductGroup(
        @PathVariable("id") UUID productGroupId
    ) {
        log.info("Restore product group: productGroupId={}", productGroupId);
        productGroupService.restoreProductGroup(productGroupId);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("product-group.restore.success"))
            .build();
    }

    @PatchMapping("/bulk-action")
    @Operation(summary = "Bulk action product group")
    @PreAuthorize("hasAnyAuthority('PRODUCT_GROUP_UPDATE', 'PRODUCT_GROUP_TRASH', 'PRODUCT_GROUP_DELETE')")
    ApiResponse<Void> bulkAction(
        @Valid @RequestBody BulkActionRequest request
    ) {
        productGroupService.bulkAction(request);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("product-group.bulk-action.success"))
            .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product group")
    @PreAuthorize("hasAuthority('PRODUCT_GROUP_DELETE')")
    ApiResponse<Void> deleteProductGroup(
        @PathVariable("id") UUID productGroupId
    ) {
        log.info("Delete product group: productGroupId={}", productGroupId);
        productGroupService.deleteProductGroup(productGroupId);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("product-group.delete.success"))
            .build();
    }

}
