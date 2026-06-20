package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.config.Translator;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.category.CategoryFilterRequest;
import com.khahnm04.ecommerce.dto.request.category.CategoryRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.category.CategoryResponse;
import com.khahnm04.ecommerce.service.category.CategoryService;
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

@Slf4j(topic = "CATEGORY-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/admin/categories")
@RequiredArgsConstructor
@Tag(name = "Admin Category Controller", description = "APIs for managing categories")
public class AdminCategoryController {

    private final CategoryService categoryService;
    private final Translator translator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create category")
    @PreAuthorize("hasAuthority('CATEGORY_CREATE')")
    ApiResponse<CategoryResponse> createCategory(
        @Valid @RequestBody CategoryRequest request
    ) {
        log.info("Create category");
        return ApiResponse.<CategoryResponse>builder()
            .data(categoryService.createCategory(request))
            .message(translator.toLocale("CATEGORY_CREATED_SUCCESS"))
            .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category detail")
    @PreAuthorize("hasAuthority('CATEGORY_VIEW')")
    ApiResponse<CategoryResponse> getCategoryDetailById(
        @PathVariable Long id,
        @RequestParam(defaultValue = "false") boolean isDeleted
    ) {
        log.info("Get category detail");
        return ApiResponse.<CategoryResponse>builder()
            .data(categoryService.getCategoryDetailById(id, isDeleted))
            .message(translator.toLocale("CATEGORY_GET_DETAIL_SUCCESS"))
            .build();
    }

    @GetMapping
    @Operation(summary = "Get all categories")
    @PreAuthorize("hasAuthority('CATEGORY_VIEW')")
    PageResponse<CategoryResponse> getAllCategories(
        @Valid @ModelAttribute CategoryFilterRequest request
    ) {
        log.info("Get all categories");
        Page<CategoryResponse> page = categoryService.getAllCategories(request);

        return PageResponse.<CategoryResponse>builder()
            .data(page.getContent())
            .meta(PageResponse.Meta.fromPage(page))
            .message(translator.toLocale("CATEGORY_GET_ALL_SUCCESS"))
            .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category")
    @PreAuthorize("hasAuthority('CATEGORY_UPDATE')")
    ApiResponse<CategoryResponse> updateCategory(
        @PathVariable Long id,
        @Valid @RequestBody CategoryRequest request
    ) {
        log.info("Update category");
        return ApiResponse.<CategoryResponse>builder()
            .data(categoryService.updateCategory(id, request))
            .message(translator.toLocale("CATEGORY_UPDATED_SUCCESS"))
            .build();
    }

    @PatchMapping("/{id}/soft-delete")
    @Operation(summary = "Soft delete category")
    @PreAuthorize("hasAuthority('CATEGORY_DELETE')")
    ApiResponse<Void> softDeleteCategory(
        @PathVariable Long id
    ) {
        log.info("Soft delete category");
        categoryService.softDeleteCategory(id);

        return ApiResponse.<Void>builder()
            .message(translator.toLocale("CATEGORY_SOFT_DELETED_SUCCESS"))
            .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category")
    @PreAuthorize("hasAuthority('CATEGORY_DELETE')")
    ApiResponse<Void> deleteCategory(
        @PathVariable Long id
    ) {
        log.info("Delete category");
        categoryService.deleteCategory(id);

        return ApiResponse.<Void>builder()
            .message(translator.toLocale("CATEGORY_DELETED_SUCCESS"))
            .build();
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Restore category")
    @PreAuthorize("hasAuthority('CATEGORY_DELETE')")
    ApiResponse<Void> restoreCategory(
        @PathVariable Long id
    ) {
        log.info("Restore category");
        categoryService.restoreCategory(id);

        return ApiResponse.<Void>builder()
            .message(translator.toLocale("CATEGORY_RESTORED_SUCCESS"))
            .build();
    }

    @PatchMapping("/bulk")
    @Operation(summary = "Bulk action categories")
    @PreAuthorize("hasAnyAuthority('CATEGORY_UPDATE', 'CATEGORY_DELETE')")
    ApiResponse<Void> bulkAction(
        @RequestBody BulkActionRequest request
    ) {
        categoryService.bulkAction(request);

        return ApiResponse.<Void>builder()
            .message(translator.toLocale("CATEGORY_BULK_ACTION_SUCCESS"))
            .build();
    }

}
