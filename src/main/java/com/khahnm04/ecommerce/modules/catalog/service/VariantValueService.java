package com.khahnm04.ecommerce.modules.catalog.service;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.catalog.dto.request.VariantValueBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.VariantValueFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.VariantValueRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.VariantValueResponse;

public interface VariantValueService {

    VariantValueResponse createVariantValue(VariantValueRequest request);

    VariantValueResponse getVariantValueDetail(Long id, boolean deleted);

    PageResponse<VariantValueResponse> getAllVariantValues(VariantValueFilterRequest filter);

    PageResponse<VariantValueResponse> getAllDeletedVariantValues(VariantValueFilterRequest filter);

    VariantValueResponse updateVariantValue(Long id, VariantValueRequest request);

    void softDeleteVariantValue(Long id);

    void restoreVariantValue(Long id);

    void handleBulkAction(VariantValueBulkActionRequest request);

}
