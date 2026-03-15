package com.khahnm04.ecommerce.modules.catalog.mapper;

import com.khahnm04.ecommerce.modules.catalog.dto.request.VariantValueRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.VariantValueResponse;
import com.khahnm04.ecommerce.modules.catalog.entity.VariantValue;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VariantValueMapper {

    @Mapping(target = "variant", ignore = true)
    VariantValue toVariantValue(VariantValueRequest request);

    @Mapping(target = "variantId", source = "variant.id")
    @Mapping(target = "variant", source = "variant.name")
    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    VariantValueResponse toVariantValueResponse(VariantValue variantValue);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "variant", ignore = true)
    void updateVariantValue(@MappingTarget VariantValue variantValue, VariantValueRequest request);

}
