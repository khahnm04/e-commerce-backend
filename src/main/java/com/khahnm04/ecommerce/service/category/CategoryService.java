package com.khahnm04.ecommerce.service.category;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.category.CategoryFilterRequest;
import com.khahnm04.ecommerce.dto.request.category.CategoryRequest;
import com.khahnm04.ecommerce.dto.response.category.CategoryResponse;
import org.springframework.data.domain.Page;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse getCategoryDetailById(Long id, boolean isDeleted);

    Page<CategoryResponse> getAllCategories(CategoryFilterRequest filter);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    void softDeleteCategory(Long id);

    void deleteCategory(Long id);

    void restoreCategory(Long id);

    void bulkAction(BulkActionRequest request);

}
