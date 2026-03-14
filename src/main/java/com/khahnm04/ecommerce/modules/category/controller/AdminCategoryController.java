package com.khahnm04.ecommerce.modules.category.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.khahnm04.ecommerce.common.base.dto.ApiResponse;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.modules.category.dto.request.CategoryFilterRequest;
import com.khahnm04.ecommerce.modules.category.dto.request.CategoryRequest;
import com.khahnm04.ecommerce.modules.category.dto.response.CategoryResponse;
import com.khahnm04.ecommerce.modules.category.enums.CategoryStatus;
import com.khahnm04.ecommerce.modules.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CategoryResponse> createCategory (
            @Valid @RequestBody CategoryRequest request
    ) {
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.createCategory(request))
                .message("Category created successfully")
                .build();
    }

    @GetMapping("/{identifier}")
    public ApiResponse<CategoryResponse> getCategoryDetail (
            @PathVariable String identifier,
            @RequestParam(defaultValue = "false") boolean deleted
    ) {
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.getCategoryDetail(identifier, deleted))
                .message("Get category detail successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategories (
            @Valid @ModelAttribute CategoryFilterRequest filter
    ) {
        PageResponse<CategoryResponse> pageResponse = categoryService.getAllCategories(filter);
        return ApiResponse.<List<CategoryResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get all categories successfully")
                .build();
    }

    @GetMapping("/deleted")
    public ApiResponse<List<CategoryResponse>> getAllDeletedCategories (
            @Valid @ModelAttribute CategoryFilterRequest filter
    ) {
        PageResponse<CategoryResponse> pageResponse = categoryService.getAllDeletedCategories(filter);
        return ApiResponse.<List<CategoryResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get deleted categories successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> updateCategory (
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request
    ) {
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.updateCategory(id, request))
                .message("Category updated successfully")
                .build();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateCategoryStatus (
            @PathVariable Long id,
            @RequestBody JsonNode request
    ) {
        JsonNode statusNode = request.get("status");
        if (statusNode == null || statusNode.isNull()) {
            throw new AppException(ErrorCode.CATEGORY_STATUS_REQUIRED);
        }
        CategoryStatus status;
        try {
            status = CategoryStatus.valueOf(statusNode.asText().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.CATEGORY_STATUS_INVALID);
        }
        categoryService.updateCategoryStatus(id, status);
        return ApiResponse.<Void>builder()
                .message("Category status updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDeleteCategory (
            @PathVariable Long id
    ) {
        categoryService.softDeleteCategory(id);
        return ApiResponse.<Void>builder()
                .message("Category soft deleted successfully")
                .build();
    }

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restoreCategory (
            @PathVariable Long id
    ) {
        categoryService.restoreCategory(id);
        return ApiResponse.<Void>builder()
                .message("Category restored successfully")
                .build();
    }

    @PatchMapping("/bulk")
    public ApiResponse<Void> handleBulkAction (
            @RequestBody JsonNode request
    ) {
        JsonNode categoryIdsNode = request.get("categoryIds");
        if (categoryIdsNode == null || !categoryIdsNode.isArray()) {
            throw new AppException(ErrorCode.CATEGORY_SELECTION_REQUIRED);
        }
        Set<Long> categoryIds = new HashSet<>();
        categoryIdsNode.forEach(node -> categoryIds.add(node.asLong()));
        if (categoryIds.isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_SELECTION_REQUIRED);
        }

        JsonNode actionNode = request.get("action");
        if (actionNode == null || actionNode.asText().isBlank()) {
            throw new AppException(ErrorCode.CATEGORY_ACTION_REQUIRED);
        }
        String action = actionNode.asText();

        categoryService.handleBulkAction(categoryIds, action);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
