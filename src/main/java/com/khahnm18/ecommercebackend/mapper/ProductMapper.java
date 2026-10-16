package com.khahnm18.ecommercebackend.mapper;

import com.khahnm18.ecommercebackend.dto.request.product.ProductRequest;
import com.khahnm18.ecommercebackend.dto.response.product.ProductResponse;
import com.khahnm18.ecommercebackend.entity.product.Product;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import java.math.BigDecimal;

@Mapper(
    componentModel = "spring",
    imports = BigDecimal.class,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProductMapper {

    @Mapping(target = "status", defaultValue = "ACTIVE")
    @Mapping(target = "isDefault", defaultValue = "false")
    @Mapping(target = "stock", defaultValue = "0")
    @Mapping(target = "priceOld", defaultExpression = "java(BigDecimal.ZERO)")
    @Mapping(target = "priceNew", defaultExpression = "java(BigDecimal.ZERO)")
    Product toProduct(ProductRequest request);

    ProductResponse toProductResponse(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProduct(@MappingTarget Product product, ProductRequest request);

}
