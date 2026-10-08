package com.khahnm18.ecommercebackend.mapper;

import com.khahnm18.ecommercebackend.dto.request.product.ProductGroupRequest;
import com.khahnm18.ecommercebackend.dto.response.category.CategoryResponse;
import com.khahnm18.ecommercebackend.dto.response.product.ProductGroupResponse;
import com.khahnm18.ecommercebackend.entity.category.Category;
import com.khahnm18.ecommercebackend.entity.product.ProductGroup;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class ProductGroupMapper {

    public ProductGroup toProductGroup(ProductGroupRequest request) {
        if (request == null) return null;
        return ProductGroup.builder()
            .name(request.name())
            .slug(request.slug())
            .images(request.images() != null ? new ArrayList<>(request.images()) : new ArrayList<>())
            .build();
    }

    public ProductGroupResponse toProductGroupResponse(ProductGroup productGroup) {
        if (productGroup == null) return null;
        return buildProductGroupResponse(productGroup).build();
    }

    public ProductGroupResponse toProductGroupResponseWithCategories(ProductGroup productGroup, List<Category> categories) {
        if (productGroup == null) return null;
        List<CategoryResponse> categoryResponses = (categories == null)
            ? Collections.emptyList()
            : categories.stream()
                .filter(Objects::nonNull)
                .map(category -> CategoryResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .slug(category.getSlug())
                    .build())
                .toList();
        return buildProductGroupResponse(productGroup)
            .categories(categoryResponses)
            .build();
    }

    public void updateProductGroup(ProductGroup productGroup, ProductGroupRequest request) {
        if (request.name() != null) {
            productGroup.setName(request.name());
        }
        if (request.slug() != null) {
            productGroup.setSlug(request.slug());
        }
        if (request.images() != null) {
            productGroup.setImages(new ArrayList<>(request.images()));
        }
    }

    private ProductGroupResponse.ProductGroupResponseBuilder buildProductGroupResponse(ProductGroup productGroup) {
        return ProductGroupResponse.builder()
            .id(productGroup.getId())
            .name(productGroup.getName())
            .slug(productGroup.getSlug())
            .images(productGroup.getImages())
            .createdAt(productGroup.getCreatedAt())
            .createdBy(productGroup.getCreatedBy())
            .updatedAt(productGroup.getUpdatedAt())
            .updatedBy(productGroup.getUpdatedBy())
            .deletedAt(productGroup.getDeletedAt())
            .deletedBy(productGroup.getDeletedBy())
            .isDeleted(productGroup.getIsDeleted());
    }

}
