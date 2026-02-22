package com.khahnm04.ecommerce.modules.inventory.service;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.request.TransferDetailRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.TransferOrderFilterRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.TransferOrderRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.response.TransferOrderResponse;

public interface TransferOrderService {

    TransferOrderResponse createTransferOrder(TransferOrderRequest request);

    PageResponse<TransferOrderResponse> getAllTransferOrders(TransferOrderFilterRequest request);

    TransferOrderResponse getTransferOrderDetailById(Long id);

    TransferOrderResponse updateTransferOrder(Long id, TransferOrderRequest request);

    TransferOrderResponse addItemToTransferOrder(Long id, TransferDetailRequest request);

    void removeItemFromTransferOrder(Long transferOrderId, Long detailId);

    void startShipping(Long id);

    void completeTransferOrder(Long id);

    void cancelTransferOrder(Long id);

}
