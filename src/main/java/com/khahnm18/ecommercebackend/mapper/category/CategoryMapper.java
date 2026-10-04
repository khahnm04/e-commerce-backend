package com.khahnm18.ecommercebackend.mapper.category;

import com.khahnm18.ecommercebackend.constant.category.CategoryStatus;
import com.khahnm18.ecommercebackend.dto.request.category.CategoryRequest;
import com.khahnm18.ecommercebackend.dto.response.category.CategoryResponse;
import com.khahnm18.ecommercebackend.entity.category.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toCategory(CategoryRequest request) {
        if (request == null) return null;
        return Category.builder()
            .parentId(request.parentId())
            .name(request.name())
            .slug(request.slug())
            .avatar(request.avatar())
            .description(request.description())
            .status(request.status() != null ? request.status() : CategoryStatus.ACTIVE)
            .build();
    }

    public CategoryResponse toCategoryResponse(Category category) {
        if (category == null) return null;
        return CategoryResponse.builder()
            .id(category.getId())
            .parentId(category.getParentId())
            .name(category.getName())
            .slug(category.getSlug())
            .avatar(category.getAvatar())
            .description(category.getDescription())
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

    public void updateCategory(Category category, CategoryRequest request) {
        category.setParentId(request.parentId());
        if (request.name() != null) {
            category.setName(request.name());
        }
        if (request.slug() != null) {
            category.setSlug(request.slug());
        }
        if (request.avatar() != null) {
            category.setAvatar(request.avatar());
        }
        if (request.description() != null) {
            category.setDescription(request.description());
        }
        if (request.status() != null) {
            category.setStatus(request.status());
        }
    }

}
