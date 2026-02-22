package com.khahnm04.ecommerce.modules.inventory.mapper;

import com.khahnm04.ecommerce.modules.inventory.dto.response.ImportOrderResponse;
import com.khahnm04.ecommerce.modules.inventory.entity.ImportOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImportOrderMapper {

    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "warehouseName", source = "warehouse.name")
    @Mapping(target = "supplierId", source = "supplier.id")
    @Mapping(target = "supplierName", source = "supplier.name")
    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    @Mapping(target = "details", ignore = true)
    ImportOrderResponse toImportOrderResponse(ImportOrder importOrder);

}
