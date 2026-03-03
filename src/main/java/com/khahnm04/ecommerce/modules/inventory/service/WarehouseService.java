package com.khahnm04.ecommerce.modules.inventory.service;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.request.WarehouseBulkActionRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.WarehouseFilterRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.WarehouseRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.WarehouseUpdateStatusRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.response.WarehouseResponse;
import com.khahnm04.ecommerce.modules.inventory.entity.Warehouse;

public interface WarehouseService {

    WarehouseResponse createWarehouse(WarehouseRequest request);

    PageResponse<WarehouseResponse> getAllWarehouses(WarehouseFilterRequest filter);

    PageResponse<WarehouseResponse> getAllDeletedWarehouses(WarehouseFilterRequest filter);

    Warehouse getWarehouseById(Long id);

    WarehouseResponse getWarehouseDetailById(Long id);

    WarehouseResponse updateWarehouse(Long id, WarehouseRequest request);

    void updateWarehouseStatus(Long id, WarehouseUpdateStatusRequest request);

    void softDeleteWarehouse(Long id);

    void restoreWarehouse(Long id);

    void handleBulkAction(WarehouseBulkActionRequest request);

}
