package com.khahnm04.ecommerce.mapper.category;

import com.khahnm04.ecommerce.constant.category.CategoryStatus;
import com.khahnm04.ecommerce.dto.projection.CategoryProjection;
import com.khahnm04.ecommerce.dto.request.category.CategoryRequest;
import com.khahnm04.ecommerce.dto.response.category.CategoryResponse;
import com.khahnm04.ecommerce.entity.category.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toCategory(CategoryRequest request) {
        return Category.builder()
            .parentId(request.parentId())
            .slug(request.slug())
            .name(request.name())
            .thumbnail(request.thumbnail())
            .isFeatured(request.isFeatured())
            .showInMenu(request.showInMenu())
            .status(request.status() == null ? CategoryStatus.ACTIVE : request.status())
            .build();
    }

    public CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder()
            .id(category.getId())
            .parentId(category.getParentId())
            .slug(category.getSlug())
            .name(category.getName())
            .thumbnail(category.getThumbnail())
            .isFeatured(category.getIsFeatured())
            .showInMenu(category.getShowInMenu())
            .status(category.getStatus())
            .createdAt(category.getCreatedAt())
            .createdBy(category.getCreatedBy())
            .updatedAt(category.getUpdatedAt())
            .updatedBy(category.getUpdatedBy())
            .deletedAt(category.getDeletedAt())
            .deletedBy(category.getDeletedBy())
            .isDeleted(category.getIsDeleted())
            .build();
    }

    public CategoryResponse toCategoryResponse(CategoryProjection projection) {
        return CategoryResponse.builder()
            .id(projection.getId())
            .parentId(projection.getParentId())
            .name(projection.getName())
            .slug(projection.getSlug())
            .thumbnail(projection.getThumbnail())
            .isFeatured(projection.getIsFeatured())
            .showInMenu(projection.getShowInMenu())
            .status(projection.getStatus())
            .createdAt(projection.getCreatedAt())
            .createdBy(projection.getCreatedBy())
            .updatedAt(projection.getUpdatedAt())
            .updatedBy(projection.getUpdatedBy())
            .deletedAt(projection.getDeletedAt())
            .deletedBy(projection.getDeletedBy())
            .isDeleted(projection.getIsDeleted())
            .build();
    }

    public void updateCategory(Category category, CategoryRequest request) {
        if (request.name() != null) {
            category.setName(request.name());
        }
        if (request.slug() != null) {
            category.setSlug(request.slug());
        }
        if (request.thumbnail() != null) {
            category.setThumbnail(request.thumbnail());
        }
        if (request.isFeatured() != null) {
            category.setIsFeatured(request.isFeatured());
        }
        if (request.showInMenu() != null) {
            category.setShowInMenu(request.showInMenu());
        }
        if (request.status() != null) {
            category.setStatus(request.status());
        }
    }

}
