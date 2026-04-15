package com.khahnm04.ecommerce.mapper;

import com.khahnm04.ecommerce.dto.request.ProductVariantRequest;
import com.khahnm04.ecommerce.dto.response.ProductVariantResponse;
import com.khahnm04.ecommerce.entity.ProductVariant;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductVariantMapper {

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "sku", ignore = true)
    ProductVariant toProductVariant(ProductVariantRequest request);

    @Mapping(target = "product", source = "product.name")
    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    @Mapping(target = "deletedBy", source = "deletedBy.fullName")
    @Mapping(target = "variantValues", ignore = true)
    ProductVariantResponse toProductVariantResponse(ProductVariant productVariant);

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "sku", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductVariant(@MappingTarget ProductVariant productVariant, ProductVariantRequest request);

}
