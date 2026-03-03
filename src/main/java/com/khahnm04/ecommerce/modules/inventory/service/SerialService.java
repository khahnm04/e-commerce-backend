package com.khahnm04.ecommerce.modules.inventory.service;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.request.SerialFilterRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.SerialUpdateNotesRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.SerialUpdateStatusRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.response.SerialMovementLogResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.response.SerialNumberResponse;
import java.util.List;

public interface SerialService {

    PageResponse<SerialNumberResponse> getAllSerials(SerialFilterRequest request);

    SerialNumberResponse getSerialDetailById(Long id);

    SerialNumberResponse checkSerialByCode(String serialCode);

    List<SerialNumberResponse> getSerialsByWarehouse(Long warehouseId);

    List<SerialNumberResponse> getSerialsByVariant(Long productVariantId);

    void updateSerialStatus(Long id, SerialUpdateStatusRequest request);

    void updateSerialNotes(Long id, SerialUpdateNotesRequest request);

    List<SerialMovementLogResponse> getSerialMovements(Long id);

    List<SerialNumberResponse> getAvailableSerials(Long productVariantId, Long warehouseId);

}
