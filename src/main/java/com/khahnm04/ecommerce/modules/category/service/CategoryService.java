package com.khahnm04.ecommerce.modules.category.service;

import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.modules.category.dto.request.CategoryFilterRequest;
import com.khahnm04.ecommerce.modules.category.dto.request.CategoryRequest;
import com.khahnm04.ecommerce.modules.category.dto.response.CategoryResponse;
import com.khahnm04.ecommerce.modules.category.entity.Category;
import com.khahnm04.ecommerce.modules.category.enums.CategoryStatus;

import java.util.Set;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    Category getCategoryById(Long id);

    CategoryResponse getCategoryDetail(String identifier, boolean deleted);

    PageResponse<CategoryResponse> getAllCategories(CategoryFilterRequest filter);

    PageResponse<CategoryResponse> getAllDeletedCategories(CategoryFilterRequest filter);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    void updateCategoryStatus(Long id, CategoryStatus status);

    void softDeleteCategory(Long id);

    void restoreCategory(Long id);

    void handleBulkAction(Set<Long> categoryIds, String action);

}
