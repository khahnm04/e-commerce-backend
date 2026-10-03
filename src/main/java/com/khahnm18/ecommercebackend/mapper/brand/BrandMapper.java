package com.khahnm18.ecommercebackend.mapper.brand;

import com.khahnm18.ecommercebackend.constant.brand.BrandStatus;
import com.khahnm18.ecommercebackend.dto.request.brand.BrandRequest;
import com.khahnm18.ecommercebackend.dto.response.brand.BrandResponse;
import com.khahnm18.ecommercebackend.entity.brand.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public Brand toBrand(BrandRequest request) {
        if (request == null) return null;
        return Brand.builder()
            .name(request.name())
            .slug(request.slug())
            .avatar(request.avatar())
            .description(request.description())
            .status(request.status() != null ? request.status() : BrandStatus.ACTIVE)
            .build();
    }

    public BrandResponse toBrandResponse(Brand brand) {
        if (brand == null) return null;
        return BrandResponse.builder()
            .id(brand.getId())
            .name(brand.getName())
            .slug(brand.getSlug())
            .avatar(brand.getAvatar())
            .description(brand.getDescription())
            .status(brand.getStatus())
            .createdAt(brand.getCreatedAt())
            .createdBy(brand.getCreatedBy())
            .updatedAt(brand.getUpdatedAt())
            .updatedBy(brand.getUpdatedBy())
            .deletedAt(brand.getDeletedAt())
            .deletedBy(brand.getDeletedBy())
            .isDeleted(brand.getIsDeleted())
            .build();
    }

    public void updateBrand(Brand brand, BrandRequest request) {
        if (request.name() != null) {
            brand.setName(request.name());
        }
        if (request.slug() != null) {
            brand.setSlug(request.slug());
        }
        if (request.avatar() != null) {
            brand.setAvatar(request.avatar());
        }
        if (request.description() != null) {
            brand.setDescription(request.description());
        }
        if (request.status() != null) {
            brand.setStatus(request.status());
        }
    }

}
