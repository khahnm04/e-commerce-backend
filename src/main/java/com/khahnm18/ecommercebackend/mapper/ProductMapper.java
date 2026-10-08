package com.khahnm18.ecommercebackend.mapper;

import com.khahnm18.ecommercebackend.dto.request.product.ProductRequest;
import com.khahnm18.ecommercebackend.dto.response.product.ProductResponse;
import com.khahnm18.ecommercebackend.entity.product.Product;
import com.khahnm18.ecommercebackend.enums.ProductStatus;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class ProductMapper {

    public Product toProduct(ProductRequest request) {
        if (request == null) return null;
        return Product.builder()
            .versionId(request.versionId())
            .name(request.name())
            .slug(request.slug())
            .color(request.color())
            .sku(request.sku())
            .isDefault(request.isDefault() != null && request.isDefault())
            .priceOld(request.priceOld() != null ? request.priceOld() : BigDecimal.ZERO)
            .priceNew(request.priceNew() != null ? request.priceNew() : BigDecimal.ZERO)
            .stock(request.stock() != null ? request.stock() : 0)
            .avatar(request.avatar())
            .status(request.status() != null ? request.status() : ProductStatus.ACTIVE)
            .build();
    }

    public ProductResponse toProductResponse(Product product) {
        if (product == null) return null;
        return ProductResponse.builder()
            .id(product.getId())
            .versionId(product.getVersionId())
            .name(product.getName())
            .slug(product.getSlug())
            .color(product.getColor())
            .sku(product.getSku())
            .isDefault(product.getIsDefault())
            .priceOld(product.getPriceOld())
            .priceNew(product.getPriceNew())
            .discount(product.getDiscount())
            .stock(product.getStock())
            .avatar(product.getAvatar())
            .status(product.getStatus())
            .createdAt(product.getCreatedAt())
            .createdBy(product.getCreatedBy())
            .updatedAt(product.getUpdatedAt())
            .updatedBy(product.getUpdatedBy())
            .deletedAt(product.getDeletedAt())
            .deletedBy(product.getDeletedBy())
            .isDeleted(product.getIsDeleted())
            .build();
    }

    public void updateProduct(Product product, ProductRequest request) {
        if (request.versionId() != null) {
            product.setVersionId(request.versionId());
        }
        if (request.name() != null) {
            product.setName(request.name());
        }
        if (request.slug() != null) {
            product.setSlug(request.slug());
        }
        if (request.color() != null) {
            product.setColor(request.color());
        }
        if (request.sku() != null) {
            product.setSku(request.sku());
        }
        if (request.isDefault() != null) {
            product.setIsDefault(request.isDefault());
        }
        if (request.priceOld() != null) {
            product.setPriceOld(request.priceOld());
        }
        if (request.priceNew() != null) {
            product.setPriceNew(request.priceNew());
        }
        if (request.stock() != null) {
            product.setStock(request.stock());
        }
        if (request.avatar() != null) {
            product.setAvatar(request.avatar());
        }
        if (request.status() != null) {
            product.setStatus(request.status());
        }
    }

}
