package com.khahnm04.ecommerce.modules.product.mapper;

import com.khahnm04.ecommerce.modules.product.dto.request.ProductRequest;
import com.khahnm04.ecommerce.modules.product.dto.response.ProductResponse;
import com.khahnm04.ecommerce.modules.product.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "brand", ignore = true)
    Product toProduct(ProductRequest request);

    @Mapping(target = "images", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "attributes", ignore = true)
    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "brandName", source = "brand.name")
    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    ProductResponse toProductResponse(Product product);

    @Mapping(target = "brand", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProduct(@MappingTarget Product product, ProductRequest request);

}
