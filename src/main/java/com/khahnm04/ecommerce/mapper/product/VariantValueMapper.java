package com.khahnm04.ecommerce.mapper.product;

import com.khahnm04.ecommerce.dto.request.product.VariantValueRequest;
import com.khahnm04.ecommerce.dto.response.product.VariantValueResponse;
import com.khahnm04.ecommerce.entity.product.VariantValue;
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
