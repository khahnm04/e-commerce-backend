package com.khahnm04.ecommerce.mapper.product;

import com.khahnm04.ecommerce.dto.request.product.VariantRequest;
import com.khahnm04.ecommerce.dto.response.product.VariantResponse;
import com.khahnm04.ecommerce.entity.product.Variant;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VariantMapper {

    Variant toVariant(VariantRequest request);

    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    VariantResponse toVariantResponse(Variant variant);

    void updateVariant(@MappingTarget Variant variant, VariantRequest request);

}
