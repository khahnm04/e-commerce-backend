package com.khahnm04.ecommerce.modules.catalog.service;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.catalog.dto.request.CategoryBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.CategoryFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.CategoryRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.CategoryUpdateStatusRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.CategoryResponse;
import com.khahnm04.ecommerce.modules.catalog.entity.Category;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    PageResponse<CategoryResponse> getAllCategories(CategoryFilterRequest filter);

    PageResponse<CategoryResponse> getAllDeletedCategories(CategoryFilterRequest filter);

    Category getCategoryById(Long id);

    CategoryResponse getCategoryDetailById(Long id);

    CategoryResponse getCategoryDetailBySlug(String slug);

    CategoryResponse getDeletedCategoryDetailById(Long id);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    void updateCategoryStatus(Long id, CategoryUpdateStatusRequest status);

    void softDeleteCategory(Long id);

    void restoreCategory(Long id);

    void handleBulkAction(CategoryBulkActionRequest request);

}
