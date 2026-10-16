package com.khahnm18.ecommercebackend.controller.admin;

import com.khahnm18.ecommercebackend.enums.CategoryStatus;
import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.product.CategoryRequest;
import com.khahnm18.ecommercebackend.dto.response.ApiResponse;
import com.khahnm18.ecommercebackend.dto.response.PageResponse;
import com.khahnm18.ecommercebackend.dto.response.product.CategoryResponse;
import com.khahnm18.ecommercebackend.service.product.CategoryService;
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
import java.time.Instant;
import java.util.UUID;

@Slf4j(topic = "CATEGORY-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
@Tag(name = "Category Controller", description = "Category management APIs")
public class AdminCategoryController {

    private final Translator translator;
    private final CategoryService categoryService;

    @GetMapping("/{id}")
    @Operation(summary = "Get category detail by id")
    @PreAuthorize("hasAuthority('CATEGORY_VIEW')")
    ApiResponse<CategoryResponse> getCategoryDetailById(
        @PathVariable("id") UUID categoryId,
        @RequestParam(defaultValue = "false") boolean isDeleted
    ) {
        log.info("Get category detail: categoryId={}", categoryId);
        return ApiResponse.<CategoryResponse>builder()
            .status(HttpStatus.OK.value())
            .data(categoryService.getCategoryDetailById(categoryId, isDeleted))
            .message(translator.toLocale("category.detail.success"))
            .build();
    }

    @GetMapping
    @Operation(summary = "Get all categories")
    @PreAuthorize("hasAuthority('CATEGORY_VIEW')")
    PageResponse<CategoryResponse> getAllCategories(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "false") boolean isDeleted,
        @RequestParam(defaultValue = "createdAt,desc") String sort,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Instant startDate,
        @RequestParam(required = false) Instant endDate,
        @RequestParam(required = false) CategoryStatus status,
        @RequestParam(required = false) UUID parentId
    ) {
        log.info("Get all categories");
        Page<CategoryResponse> categoryPage = categoryService.getAllCategories(
            page,
            size,
            sort,
            keyword,
            isDeleted,
            startDate,
            endDate,
            status,
            parentId
        );
        return PageResponse.<CategoryResponse>builder()
            .status(HttpStatus.OK.value())
            .data(categoryPage.getContent())
            .meta(PageResponse.Meta.fromPage(categoryPage))
            .message(translator.toLocale("category.list.success"))
            .build();
    }

    @PostMapping
    @Operation(summary = "Create category")
    @PreAuthorize("hasAuthority('CATEGORY_CREATE')")
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<CategoryResponse> createCategory(
        @Valid @RequestBody CategoryRequest request
    ) {
        log.info("Create category");
        return ApiResponse.<CategoryResponse>builder()
            .status(HttpStatus.CREATED.value())
            .data(categoryService.createCategory(request))
            .message(translator.toLocale("category.create.success"))
            .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category")
    @PreAuthorize("hasAuthority('CATEGORY_UPDATE')")
    ApiResponse<CategoryResponse> updateCategory(
        @PathVariable("id") UUID categoryId,
        @Valid @RequestBody CategoryRequest request
    ) {
        log.info("Update category: categoryId={}", categoryId);
        return ApiResponse.<CategoryResponse>builder()
            .status(HttpStatus.OK.value())
            .data(categoryService.updateCategory(categoryId, request))
            .message(translator.toLocale("category.update.success"))
            .build();
    }

    @PatchMapping("/{id}/soft-delete")
    @Operation(summary = "Soft delete category")
    @PreAuthorize("hasAuthority('CATEGORY_TRASH')")
    ApiResponse<Void> softDeleteCategory(
        @PathVariable("id") UUID categoryId
    ) {
        log.info("Soft delete category: categoryId={}", categoryId);
        categoryService.softDeleteCategory(categoryId);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("category.soft-delete.success"))
            .build();
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Restore category")
    @PreAuthorize("hasAuthority('CATEGORY_TRASH')")
    ApiResponse<Void> restoreCategory(
        @PathVariable("id") UUID categoryId
    ) {
        log.info("Restore category: categoryId={}", categoryId);
        categoryService.restoreCategory(categoryId);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("category.restore.success"))
            .build();
    }

    @PatchMapping("/bulk-action")
    @Operation(summary = "Bulk action category")
    @PreAuthorize("hasAnyAuthority('CATEGORY_UPDATE', 'CATEGORY_TRASH', 'CATEGORY_DELETE')")
    ApiResponse<Void> bulkAction(
        @Valid @RequestBody BulkActionRequest request
    ) {
        categoryService.bulkAction(request);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("category.bulk-action.success"))
            .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category")
    @PreAuthorize("hasAuthority('CATEGORY_DELETE')")
    ApiResponse<Void> deleteCategory(
        @PathVariable("id") UUID categoryId
    ) {
        log.info("Delete category: categoryId={}", categoryId);
        categoryService.deleteCategory(categoryId);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("category.delete.success"))
            .build();
    }

}
