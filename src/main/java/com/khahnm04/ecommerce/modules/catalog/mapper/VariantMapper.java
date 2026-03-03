package com.khahnm04.ecommerce.modules.catalog.mapper;

import com.khahnm04.ecommerce.modules.catalog.dto.request.VariantRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.VariantResponse;
import com.khahnm04.ecommerce.modules.catalog.entity.Variant;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VariantMapper {

    Variant toVariant(VariantRequest request);

    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    VariantResponse toVariantResponse(Variant variant);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateVariant(@MappingTarget Variant variant, VariantRequest request);

}
