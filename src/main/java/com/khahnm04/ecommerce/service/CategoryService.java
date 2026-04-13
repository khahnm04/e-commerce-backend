package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.CategoryFilterRequest;
import com.khahnm04.ecommerce.dto.request.CategoryRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.CategoryResponse;
import com.khahnm04.ecommerce.entity.Category;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    Category getCategoryById(Long id);

    CategoryResponse getCategoryDetail(String identifier, boolean deleted);

    PageResponse<CategoryResponse> getAllCategories(CategoryFilterRequest filter);

    PageResponse<CategoryResponse> getAllDeletedCategories(CategoryFilterRequest filter);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    void softDeleteCategory(Long id);

    void deleteCategory(Long id);

    void restoreCategory(Long id);

    void handleBulkAction(BulkActionRequest request);

}
