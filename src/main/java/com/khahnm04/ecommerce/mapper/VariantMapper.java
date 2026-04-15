package com.khahnm04.ecommerce.mapper;

import com.khahnm04.ecommerce.dto.request.VariantRequest;
import com.khahnm04.ecommerce.dto.response.VariantResponse;
import com.khahnm04.ecommerce.entity.Variant;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VariantMapper {

    Variant toVariant(VariantRequest request);

    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    @Mapping(target = "deletedBy", source = "deletedBy.fullName")
    VariantResponse toVariantResponse(Variant variant);

    void updateVariant(@MappingTarget Variant variant, VariantRequest request);

}
