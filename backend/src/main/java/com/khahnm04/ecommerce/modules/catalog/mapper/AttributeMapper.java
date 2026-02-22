package com.khahnm04.ecommerce.modules.catalog.mapper;

import com.khahnm04.ecommerce.modules.catalog.dto.request.AttributeRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.AttributeResponse;
import com.khahnm04.ecommerce.modules.catalog.entity.Attribute;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AttributeMapper {

    Attribute toAttribute(AttributeRequest request);

    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    AttributeResponse toAttributeResponse(Attribute attribute);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAttribute(@MappingTarget Attribute attribute, AttributeRequest request);

}
