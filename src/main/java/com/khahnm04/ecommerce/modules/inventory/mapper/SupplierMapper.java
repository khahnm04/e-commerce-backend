package com.khahnm04.ecommerce.modules.inventory.mapper;

import com.khahnm04.ecommerce.modules.inventory.dto.request.SupplierRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.response.SupplierResponse;
import com.khahnm04.ecommerce.modules.inventory.entity.Supplier;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SupplierMapper {

    @Mapping(target = "status", ignore = true)
    Supplier toSupplier(SupplierRequest request);

    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    SupplierResponse toSupplierResponse(Supplier supplier);

    @Mapping(target = "status", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSupplier(@MappingTarget Supplier supplier, SupplierRequest request);

}
