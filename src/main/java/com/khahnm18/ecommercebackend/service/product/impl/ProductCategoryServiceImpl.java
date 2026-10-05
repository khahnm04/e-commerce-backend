package com.khahnm18.ecommercebackend.service.product.impl;

import com.khahnm18.ecommercebackend.entity.category.Category;
import com.khahnm18.ecommercebackend.entity.product.Product;
import com.khahnm18.ecommercebackend.entity.product.ProductCategory;
import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.repository.category.CategoryRepository;
import com.khahnm18.ecommercebackend.repository.product.ProductCategoryRepository;
import com.khahnm18.ecommercebackend.service.product.ProductCategoryService;
import com.khahnm18.ecommercebackend.util.SecurityUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j(topic = "PRODUCT-CATEGORY-SERVICE")
@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    @Transactional
    public List<Category> assignCategoriesToProduct(Product product, List<UUID> categoryIds) {
        List<Category> categories = getCategoriesByCategoryIds(categoryIds);
        this.saveProductCategories(product, categories);
        return categories;
    }

    @Override
    @Transactional
    public List<Category> replaceCategoriesForProduct(Product product, List<UUID> newCategoryIds) {
        List<Category> newCategories = getCategoriesByCategoryIds(newCategoryIds);
        List<UUID> currentCategoryIds = categoryRepository.findCategoriesByProductId(product.getId())
            .stream()
            .map(Category::getId)
            .toList();

        // Remove
        List<UUID> categoryIdsToRemove = currentCategoryIds.stream()
            .filter(currentCategoryId -> !newCategoryIds.contains(currentCategoryId))
            .toList();
        if (!categoryIdsToRemove.isEmpty()) {
            productCategoryRepository.deleteByProductIdAndCategoryIdIn(product.getId(), categoryIdsToRemove);
        }

        // Add
        List<Category> categoriesToAdd = newCategories.stream()
            .filter(newCategory -> !currentCategoryIds.contains(newCategory.getId()))
            .toList();
        if (!categoriesToAdd.isEmpty()) {
            this.saveProductCategories(product, categoriesToAdd);
        }

        product.setUpdatedAt(Instant.now());
        product.setUpdatedBy(SecurityUtil.getCurrentAccount());
        return newCategories;
    }

    private List<Category> getCategoriesByCategoryIds(List<UUID> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Category> categories = categoryRepository.findAllById(categoryIds);
        if (categories.size() != categoryIds.size()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        return categories;
    }

    private void saveProductCategories(Product product, List<Category> categories) {
        List<ProductCategory> productCategories = categories.stream()
            .map(category -> ProductCategory.builder()
                .productId(product.getId())
                .categoryId(category.getId())
                .build())
            .toList();
        productCategoryRepository.saveAll(productCategories);
        log.info("Assigned {} categories to product: {}", productCategories.size(), product.getName());
    }

}
