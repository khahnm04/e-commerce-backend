package com.khahnm18.ecommercebackend.mapper;

import com.khahnm18.ecommercebackend.dto.request.product.ProductVersionRequest;
import com.khahnm18.ecommercebackend.dto.response.product.ProductVersionResponse;
import com.khahnm18.ecommercebackend.entity.product.ProductVersion;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
    collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE
)
public interface ProductVersionMapper {

    ProductVersion toProductVersion(ProductVersionRequest request);

    ProductVersionResponse toProductVersionResponse(ProductVersion productVersion);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductVersion(@MappingTarget ProductVersion productVersion, ProductVersionRequest request);

}
