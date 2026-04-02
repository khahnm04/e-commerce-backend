package com.khahnm04.ecommerce.mapper.product;

import com.khahnm04.ecommerce.dto.request.product.AttributeRequest;
import com.khahnm04.ecommerce.dto.response.product.AttributeResponse;
import com.khahnm04.ecommerce.entity.product.Attribute;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AttributeMapper {

    Attribute toAttribute(AttributeRequest request);

    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    AttributeResponse toAttributeResponse(Attribute attribute);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAttribute(@MappingTarget Attribute attribute, AttributeRequest request);

}
