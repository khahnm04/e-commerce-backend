package com.khahnm04.ecommerce.modules.catalog.controller;

import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.catalog.dto.request.CategoryBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.CategoryFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.CategoryRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.CategoryUpdateStatusRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.CategoryResponse;
import com.khahnm04.ecommerce.modules.catalog.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest request
    ) {
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.createCategory(request))
                .message("Category created successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategories(
            @Valid @ModelAttribute CategoryFilterRequest filter
    ) {
        PageResponse<CategoryResponse> pageResponse = categoryService.getAllCategories(filter);
        return ApiResponse.<List<CategoryResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get all categories successfully")
                .build();
    }

    @GetMapping("/deleted")
    public ApiResponse<List<CategoryResponse>> getAllDeletedCategories(
            @Valid @ModelAttribute CategoryFilterRequest filter
    ) {
        PageResponse<CategoryResponse> pageResponse = categoryService.getAllDeletedCategories(filter);
        return ApiResponse.<List<CategoryResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get deleted categories successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategoryDetailById(
            @PathVariable Long id
    ) {
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.getCategoryDetailById(id))
                .message("Get category detail successfully")
                .build();
    }

    @GetMapping("/slug/{slug}")
    public ApiResponse<CategoryResponse> getCategoryDetailBySlug(
            @PathVariable String slug
    ) {
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.getCategoryDetailBySlug(slug))
                .message("Get category detail by slug successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request
    ) {
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.updateCategory(id, request))
                .message("Category updated successfully")
                .build();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateCategoryStatus(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateStatusRequest status
    ) {
        categoryService.updateCategoryStatus(id, status);
        return ApiResponse.<Void>builder()
                .message("Category status updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDeleteCategory(
            @PathVariable Long id
    ) {
        categoryService.softDeleteCategory(id);
        return ApiResponse.<Void>builder()
                .message("Category soft deleted successfully")
                .build();
    }

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restoreCategory(
            @PathVariable Long id
    ) {
        categoryService.restoreCategory(id);
        return ApiResponse.<Void>builder()
                .message("Category restored successfully")
                .build();
    }

    @PatchMapping("/bulk")
    public ApiResponse<Void> bulkAction(
            @Valid @RequestBody CategoryBulkActionRequest request
    ) {
        categoryService.handleBulkAction(request);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
