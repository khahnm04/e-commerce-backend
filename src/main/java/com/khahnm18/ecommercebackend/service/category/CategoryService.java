package com.khahnm18.ecommercebackend.service.category;

import com.khahnm18.ecommercebackend.constant.category.CategoryStatus;
import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.category.CategoryRequest;
import com.khahnm18.ecommercebackend.dto.response.category.CategoryResponse;
import org.springframework.data.domain.Page;
import java.time.Instant;
import java.util.UUID;

public interface CategoryService {

    CategoryResponse getCategoryDetailById(UUID categoryId, boolean isDeleted);

    Page<CategoryResponse> getAllCategories(
        int page,
        int size,
        String sort,
        String keyword,
        boolean isDeleted,
        Instant startDate,
        Instant endDate,
        CategoryStatus status,
        UUID parentId
    );

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse updateCategory(UUID categoryId, CategoryRequest request);

    void softDeleteCategory(UUID categoryId);

    void restoreCategory(UUID categoryId);

    void bulkAction(BulkActionRequest request);

    void deleteCategory(UUID categoryId);

}
