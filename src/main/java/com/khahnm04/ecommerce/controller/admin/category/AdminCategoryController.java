package com.khahnm04.ecommerce.controller.admin.category;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.category.CategoryFilterRequest;
import com.khahnm04.ecommerce.dto.request.category.CategoryRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.category.CategoryResponse;
import com.khahnm04.ecommerce.service.category.CategoryService;
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
            @RequestBody BulkActionRequest request
    ) {
        categoryService.handleBulkAction(request);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
