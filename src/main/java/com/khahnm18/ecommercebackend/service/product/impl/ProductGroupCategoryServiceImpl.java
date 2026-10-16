package com.khahnm18.ecommercebackend.service.product.impl;

import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.entity.product.Category;
import com.khahnm18.ecommercebackend.entity.product.ProductGroupCategory;
import com.khahnm18.ecommercebackend.entity.product.ProductGroup;
import com.khahnm18.ecommercebackend.repository.product.CategoryRepository;
import com.khahnm18.ecommercebackend.repository.product.ProductGroupCategoryRepository;
import com.khahnm18.ecommercebackend.service.product.ProductGroupCategoryService;
import com.khahnm18.ecommercebackend.util.SecurityUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j(topic = "PRODUCT-GROUP-CATEGORY-SERVICE")
@Service
@RequiredArgsConstructor
public class ProductGroupCategoryServiceImpl implements ProductGroupCategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductGroupCategoryRepository productGroupCategoryRepository;

    @Override
    @Transactional
    public List<Category> assignCategoriesToProductGroup(ProductGroup productGroup, List<UUID> categoryIds) {
        List<Category> categories = this.getCategoriesByCategoryIds(categoryIds);
        this.saveProductCategories(productGroup, categories);
        return categories;
    }

    @Override
    @Transactional
    public List<Category> replaceCategoriesForProductGroup(ProductGroup productGroup, List<UUID> newCategoryIds) {
        List<Category> newCategories = this.getCategoriesByCategoryIds(newCategoryIds);
        List<UUID> currentCategoryIds = categoryRepository.findCategoriesByGroupId(productGroup.getId())
            .stream()
            .map(Category::getId)
            .toList();

        // Remove
        List<UUID> categoryIdsToRemove = currentCategoryIds.stream()
            .filter(currentCategoryId -> !newCategoryIds.contains(currentCategoryId))
            .toList();
        if (!categoryIdsToRemove.isEmpty()) {
            productGroupCategoryRepository.deleteByGroupIdAndCategoryIdIn(productGroup.getId(), categoryIdsToRemove);
        }

        // Add
        List<Category> categoriesToAdd = newCategories.stream()
            .filter(newCategory -> !currentCategoryIds.contains(newCategory.getId()))
            .toList();
        if (!categoriesToAdd.isEmpty()) {
            this.saveProductCategories(productGroup, categoriesToAdd);
        }

        productGroup.setUpdatedAt(Instant.now());
        productGroup.setUpdatedBy(SecurityUtil.getCurrentAccount());
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

    private void saveProductCategories(ProductGroup productGroup, List<Category> categories) {
        List<ProductGroupCategory> productCategories = categories.stream()
            .map(category -> ProductGroupCategory.builder()
                .groupId(productGroup.getId())
                .categoryId(category.getId())
                .build())
            .toList();
        productGroupCategoryRepository.saveAll(productCategories);
        log.info("Assigned {} categories to product group: {}", productCategories.size(), productGroup.getName());
    }

}
