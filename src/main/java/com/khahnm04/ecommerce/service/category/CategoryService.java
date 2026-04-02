package com.khahnm04.ecommerce.service.category;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.category.CategoryFilterRequest;
import com.khahnm04.ecommerce.dto.request.category.CategoryRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.category.CategoryResponse;
import com.khahnm04.ecommerce.entity.category.Category;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    Category getCategoryById(Long id);

    CategoryResponse getCategoryDetail(String identifier, boolean deleted);

    PageResponse<CategoryResponse> getAllCategories(CategoryFilterRequest filter);

    PageResponse<CategoryResponse> getAllDeletedCategories(CategoryFilterRequest filter);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    void softDeleteCategory(Long id);

    void restoreCategory(Long id);

    void handleBulkAction(BulkActionRequest request);

}
