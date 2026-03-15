package com.khahnm04.ecommerce.modules.catalog.mapper;

import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductVariantRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.ProductVariantResponse;
import com.khahnm04.ecommerce.modules.catalog.entity.ProductVariant;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "sku", ignore = true)
    ProductVariant toProductVariant(ProductVariantRequest request);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "product", source = "product.name")
    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    @Mapping(target = "variantValues", ignore = true)
    ProductVariantResponse toProductVariantResponse(ProductVariant productVariant);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "sku", ignore = true)
    void updateProductVariant(@MappingTarget ProductVariant productVariant, ProductVariantRequest request);

}
