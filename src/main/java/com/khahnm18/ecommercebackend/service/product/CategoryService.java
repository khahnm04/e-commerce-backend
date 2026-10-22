package com.khahnm18.ecommercebackend.service.product;

import com.khahnm18.ecommercebackend.enums.CategoryStatus;
import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.product.CategoryRequest;
import com.khahnm18.ecommercebackend.dto.request.product.CategorySearchRequest;
import com.khahnm18.ecommercebackend.dto.response.product.CategoryResponse;
import org.springframework.data.domain.Page;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryResponse getCategoryDetailById(UUID categoryId, boolean isDeleted);

    Page<CategoryResponse> getAllCategories(
        int page,
        int size,
        CategorySearchRequest request
    );

    List<CategoryResponse> getAllActiveCategories();

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse updateCategory(UUID categoryId, CategoryRequest request);

    void softDeleteCategory(UUID categoryId);

    void restoreCategory(UUID categoryId);

    void bulkAction(BulkActionRequest request);

    void deleteCategory(UUID categoryId);

}
