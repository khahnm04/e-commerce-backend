package com.khahnm04.ecommerce.modules.inventory.mapper;

import com.khahnm04.ecommerce.modules.inventory.dto.response.SerialMovementLogResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.response.SerialNumberResponse;
import com.khahnm04.ecommerce.modules.inventory.entity.SerialMovementLog;
import com.khahnm04.ecommerce.modules.inventory.entity.SerialNumber;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SerialMapper {

    @Mapping(target = "productVariantId", source = "productVariant.id")
    @Mapping(target = "productVariantSku", source = "productVariant.sku")
    @Mapping(target = "productVariantName", source = "productVariant.name")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "warehouseName", source = "warehouse.name")
    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    SerialNumberResponse toSerialNumberResponse(SerialNumber serialNumber);

    List<SerialNumberResponse> toSerialNumberResponseList(List<SerialNumber> serialNumbers);

    @Mapping(target = "fromWarehouseId", source = "fromWarehouse.id")
    @Mapping(target = "fromWarehouseName", source = "fromWarehouse.name")
    @Mapping(target = "toWarehouseId", source = "toWarehouse.id")
    @Mapping(target = "toWarehouseName", source = "toWarehouse.name")
    @Mapping(target = "createdByName", ignore = true)
    SerialMovementLogResponse toMovementLogResponse(SerialMovementLog log);

    List<SerialMovementLogResponse> toMovementLogResponseList(List<SerialMovementLog> logs);

}
