package com.khahnm04.ecommerce.mapper.brand;

import com.khahnm04.ecommerce.constant.brand.BrandStatus;
import com.khahnm04.ecommerce.dto.projection.brand.BrandProjection;
import com.khahnm04.ecommerce.dto.request.brand.BrandRequest;
import com.khahnm04.ecommerce.dto.response.brand.BrandResponse;
import com.khahnm04.ecommerce.entity.brand.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public Brand toBrand(BrandRequest request) {
        return Brand.builder()
            .name(request.name())
            .slug(request.slug())
            .logo(request.logo())
            .status(request.status() == null ? BrandStatus.ACTIVE : request.status())
            .build();
    }

    public BrandResponse toBrandResponse(Brand brand) {
        return BrandResponse.builder()
            .id(brand.getId())
            .name(brand.getName())
            .slug(brand.getSlug())
            .logo(brand.getLogo())
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

    public BrandResponse toBrandResponse(BrandProjection projection) {
        return BrandResponse.builder()
            .id(projection.getId())
            .name(projection.getName())
            .slug(projection.getSlug())
            .logo(projection.getLogo())
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

    public void updateBrand(Brand brand, BrandRequest request) {
        if (request.name() != null) {
            brand.setName(request.name());
        }
        if (request.slug() != null) {
            brand.setSlug(request.slug());
        }
        if (request.logo() != null) {
            brand.setLogo(request.logo());
        }
        if (request.status() != null) {
            brand.setStatus(request.status());
        }
    }

}
