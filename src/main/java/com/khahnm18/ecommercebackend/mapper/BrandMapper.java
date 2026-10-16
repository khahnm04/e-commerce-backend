package com.khahnm18.ecommercebackend.mapper;

import com.khahnm18.ecommercebackend.dto.request.product.BrandRequest;
import com.khahnm18.ecommercebackend.dto.response.product.BrandResponse;
import com.khahnm18.ecommercebackend.entity.product.Brand;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BrandMapper {

    @Mapping(target = "status", defaultValue = "ACTIVE")
    Brand toBrand(BrandRequest request);

    BrandResponse toBrandResponse(Brand brand);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBrand(@MappingTarget Brand brand, BrandRequest request);

}
