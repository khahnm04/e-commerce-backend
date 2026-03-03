package com.khahnm04.ecommerce.modules.inventory.service;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.request.InventoryCheckBatchUpdateRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.InventoryCheckDetailUpdateRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.InventoryCheckFilterRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.InventoryCheckRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.response.InventoryCheckResponse;

public interface InventoryCheckService {

    InventoryCheckResponse createInventoryCheck(InventoryCheckRequest request);

    PageResponse<InventoryCheckResponse> getAllInventoryChecks(InventoryCheckFilterRequest request);

    InventoryCheckResponse getInventoryCheckDetailById(Long id);

    InventoryCheckResponse updateInventoryCheck(Long id, InventoryCheckRequest request);

    InventoryCheckResponse updateDetailActualQuantity(Long checkId, Long detailId,
            InventoryCheckDetailUpdateRequest request);

    InventoryCheckResponse batchUpdateDetails(Long checkId, InventoryCheckBatchUpdateRequest request);

    void completeInventoryCheck(Long id);

    void cancelInventoryCheck(Long id);

}
