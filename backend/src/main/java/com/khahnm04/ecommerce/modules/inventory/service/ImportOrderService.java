package com.khahnm04.ecommerce.modules.inventory.service;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.request.ImportDetailRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.ImportOrderFilterRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.ImportOrderRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.response.ImportOrderResponse;

public interface ImportOrderService {

    ImportOrderResponse createImportOrder(ImportOrderRequest request);

    PageResponse<ImportOrderResponse> getAllImportOrders(ImportOrderFilterRequest request);

    ImportOrderResponse getImportOrderDetailById(Long id);

    ImportOrderResponse updateImportOrder(Long id, ImportOrderRequest request);

    ImportOrderResponse addItemToImportOrder(Long id, ImportDetailRequest request);

    void removeItemFromImportOrder(Long importOrderId, Long detailId);

    void completeImportOrder(Long id);

    void cancelImportOrder(Long id);

}
