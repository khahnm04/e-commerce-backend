package com.khahnm04.ecommerce.modules.inventory.mapper;

import com.khahnm04.ecommerce.modules.inventory.dto.request.WarehouseRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.response.WarehouseResponse;
import com.khahnm04.ecommerce.modules.inventory.entity.Warehouse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {

    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "status", ignore = true)
    Warehouse toWarehouse(WarehouseRequest request);

    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "branchName", source = "branch.name")
    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    WarehouseResponse toWarehouseResponse(Warehouse warehouse);

    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "status", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateWarehouse(@MappingTarget Warehouse warehouse, WarehouseRequest request);

}
