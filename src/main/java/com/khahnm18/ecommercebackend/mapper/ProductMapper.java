package com.khahnm18.ecommercebackend.mapper;

import com.khahnm18.ecommercebackend.dto.request.product.ProductRequest;
import com.khahnm18.ecommercebackend.entity.product.Product;
import com.khahnm18.ecommercebackend.enums.ProductStatus;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

@Component
public class ProductMapper {

    public Product toProduct(ProductRequest request) {
        if (request == null) return null;
        return Product.builder()
            .name(request.name())
            .slug(request.slug())
            .priceOld(request.priceOld() != null ? request.priceOld() : BigDecimal.ZERO)
            .priceNew(request.priceNew() != null ? request.priceNew() : BigDecimal.ZERO)
            .discount(request.discount() != null ? request.discount() : 0)
            .stock(request.stock() != null ? request.stock() : 0)
            .avatar(request.avatar())
            .images(request.images() != null ? request.images() : new ArrayList<>())
            .specifications(request.specifications() != null ? request.specifications() : new HashMap<>())
            .description(request.description())
            .content(request.content())
            .status(request.status() != null ? request.status() : ProductStatus.ACTIVE)
            .build();
    }

}
