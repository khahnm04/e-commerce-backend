package com.khahnm04.ecommerce.modules.inventory.service;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.request.SupplierBulkActionRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.SupplierFilterRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.SupplierRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.SupplierUpdateStatusRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.response.SupplierResponse;
import com.khahnm04.ecommerce.modules.inventory.entity.Supplier;

public interface SupplierService {

    SupplierResponse createSupplier(SupplierRequest request);

    PageResponse<SupplierResponse> getAllSuppliers(SupplierFilterRequest request);

    PageResponse<SupplierResponse> getAllDeletedSuppliers(SupplierFilterRequest request);

    Supplier getSupplierById(Long id);

    SupplierResponse getSupplierDetailById(Long id);

    SupplierResponse updateSupplier(Long id, SupplierRequest request);

    void updateSupplierStatus(Long id, SupplierUpdateStatusRequest request);

    void softDeleteSupplier(Long id);

    void restoreSupplier(Long id);

    void handleBulkAction(SupplierBulkActionRequest request);

}
