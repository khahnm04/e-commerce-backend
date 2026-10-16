package com.khahnm18.ecommercebackend.service.product;

import com.khahnm18.ecommercebackend.enums.CategoryStatus;
import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.product.CategoryRequest;
import com.khahnm18.ecommercebackend.dto.response.product.CategoryResponse;
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
