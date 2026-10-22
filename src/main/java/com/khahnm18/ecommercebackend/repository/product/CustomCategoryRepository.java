package com.khahnm18.ecommercebackend.repository.product;

import com.khahnm18.ecommercebackend.dto.request.product.CategorySearchRequest;
import com.khahnm18.ecommercebackend.entity.product.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CustomCategoryRepository {

    Page<Category> search(CategorySearchRequest request, Pageable pageable);

    List<Category> findActiveCategories();

}

