package com.khahnm04.ecommerce.mapper.product;

import com.khahnm04.ecommerce.dto.request.product.ProductVariantRequest;
import com.khahnm04.ecommerce.dto.response.product.ProductVariantResponse;
import com.khahnm04.ecommerce.entity.product.ProductVariant;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductVariantMapper {

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "sku", ignore = true)
    ProductVariant toProductVariant(ProductVariantRequest request);

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
