package com.khahnm04.ecommerce.modules.inventory.mapper;

import com.khahnm04.ecommerce.modules.inventory.dto.response.TransferOrderResponse;
import com.khahnm04.ecommerce.modules.inventory.entity.TransferOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransferOrderMapper {

    @Mapping(target = "fromWarehouseId", source = "fromWarehouse.id")
    @Mapping(target = "fromWarehouseName", source = "fromWarehouse.name")
    @Mapping(target = "toWarehouseId", source = "toWarehouse.id")
    @Mapping(target = "toWarehouseName", source = "toWarehouse.name")
    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    @Mapping(target = "details", ignore = true)
    TransferOrderResponse toTransferOrderResponse(TransferOrder transferOrder);

}
