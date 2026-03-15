package com.khahnm04.ecommerce.modules.catalog.service;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.catalog.dto.request.CategoryBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.CategoryFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.CategoryRequest;

import com.khahnm04.ecommerce.modules.catalog.dto.response.CategoryResponse;
import com.khahnm04.ecommerce.modules.catalog.entity.Category;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    Category getCategoryById(Long id);

    PageResponse<CategoryResponse> getAllCategories(CategoryFilterRequest filter);

    PageResponse<CategoryResponse> getAllDeletedCategories(CategoryFilterRequest filter);

    CategoryResponse getCategoryDetail(String identifier, boolean deleted);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    void softDeleteCategory(Long id);

    void restoreCategory(Long id);

    void handleBulkAction(CategoryBulkActionRequest request);

}
