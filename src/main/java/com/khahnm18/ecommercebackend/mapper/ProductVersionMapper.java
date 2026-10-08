package com.khahnm18.ecommercebackend.mapper;

import com.khahnm18.ecommercebackend.dto.request.product.ProductVersionRequest;
import com.khahnm18.ecommercebackend.dto.response.product.ProductVersionResponse;
import com.khahnm18.ecommercebackend.entity.product.ProductVersion;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public class ProductVersionMapper {

    public ProductVersion toProductVersion(ProductVersionRequest request) {
        if (request == null) return null;
        return ProductVersion.builder()
            .groupId(request.groupId())
            .name(request.name())
            .slug(request.slug())
            .version(request.version())
            .specifications(request.specifications() != null
                ? new ArrayList<>(request.specifications())
                : new ArrayList<>()
            )
            .description(request.description())
            .content(request.content())
            .build();
    }

    public ProductVersionResponse toProductVersionResponse(ProductVersion productVersion) {
        if (productVersion == null) return null;
        return ProductVersionResponse.builder()
            .id(productVersion.getId())
            .groupId(productVersion.getGroupId())
            .name(productVersion.getName())
            .slug(productVersion.getSlug())
            .version(productVersion.getVersion())
            .ratingAvg(productVersion.getRatingAvg())
            .ratingCount(productVersion.getRatingCount())
            .priceOld(productVersion.getPriceOld())
            .priceNew(productVersion.getPriceNew())
            .totalStock(productVersion.getTotalStock())
            .soldCount(productVersion.getSoldCount())
            .specifications(productVersion.getSpecifications())
            .description(productVersion.getDescription())
            .content(productVersion.getContent())
            .createdAt(productVersion.getCreatedAt())
            .createdBy(productVersion.getCreatedBy())
            .updatedAt(productVersion.getUpdatedAt())
            .updatedBy(productVersion.getUpdatedBy())
            .deletedAt(productVersion.getDeletedAt())
            .deletedBy(productVersion.getDeletedBy())
            .isDeleted(productVersion.getIsDeleted())
            .build();
    }

    public void updateProductVersion(ProductVersion productVersion, ProductVersionRequest request) {
        if (request.groupId() != null) {
            productVersion.setGroupId(request.groupId());
        }
        if (request.name() != null) {
            productVersion.setName(request.name());
        }
        if (request.slug() != null) {
            productVersion.setSlug(request.slug());
        }
        if (request.version() != null) {
            productVersion.setVersion(request.version());
        }
        if (request.specifications() != null) {
            productVersion.setSpecifications(new ArrayList<>(request.specifications()));
        }
        if (request.description() != null) {
            productVersion.setDescription(request.description());
        }
        if (request.content() != null) {
            productVersion.setContent(request.content());
        }
    }

}
