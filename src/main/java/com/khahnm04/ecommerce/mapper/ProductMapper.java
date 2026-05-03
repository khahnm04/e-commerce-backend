package com.khahnm04.ecommerce.mapper;

import com.khahnm04.ecommerce.dto.request.ProductRequest;
import com.khahnm04.ecommerce.dto.response.ProductResponse;
import com.khahnm04.ecommerce.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "productVariants", ignore = true)
    Product toProduct(ProductRequest request);

    @Mapping(target = "images", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "productVariants", ignore = true)
    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "brandName", source = "brand.name")
    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    @Mapping(target = "deletedBy", source = "deletedBy.fullName")
    ProductResponse toProductResponse(Product product);

    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "productVariants", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProduct(@MappingTarget Product product, ProductRequest request);

}
