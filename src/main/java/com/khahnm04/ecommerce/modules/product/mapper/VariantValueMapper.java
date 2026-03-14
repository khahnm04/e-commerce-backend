package com.khahnm04.ecommerce.modules.product.mapper;

import com.khahnm04.ecommerce.modules.product.dto.request.VariantValueRequest;
import com.khahnm04.ecommerce.modules.product.dto.response.VariantValueResponse;
import com.khahnm04.ecommerce.modules.product.entity.VariantValue;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VariantValueMapper {

    @Mapping(target = "variant", ignore = true)
    VariantValue toVariantValue(VariantValueRequest request);

    @Mapping(target = "variant", source = "variant.name")
    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    VariantValueResponse toVariantValueResponse(VariantValue variantValue);

    @Mapping(target = "variant", ignore = true)
    void updateVariantValue(@MappingTarget VariantValue variantValue, VariantValueRequest request);

}
