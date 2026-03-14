package com.khahnm04.ecommerce.modules.brand.mapper;

import com.khahnm04.ecommerce.modules.brand.dto.request.BrandRequest;
import com.khahnm04.ecommerce.modules.brand.dto.response.BrandResponse;
import com.khahnm04.ecommerce.modules.brand.entity.Brand;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BrandMapper {

    Brand toBrand(BrandRequest request);

    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    BrandResponse toBrandResponse(Brand brand);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBrand(@MappingTarget Brand brand, BrandRequest request);

}
