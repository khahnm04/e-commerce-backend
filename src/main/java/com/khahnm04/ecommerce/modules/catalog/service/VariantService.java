package com.khahnm04.ecommerce.modules.catalog.service;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.catalog.dto.request.VariantBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.VariantFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.VariantRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.VariantResponse;
import com.khahnm04.ecommerce.modules.catalog.entity.Variant;

public interface VariantService {

    VariantResponse createVariant(VariantRequest request);

    Variant getVariantById(Long id);

    VariantResponse getVariantDetail(String identifier, boolean deleted);

    PageResponse<VariantResponse> getAllVariants(VariantFilterRequest filter);

    PageResponse<VariantResponse> getAllDeletedVariants(VariantFilterRequest filter);

    VariantResponse updateVariant(Long id, VariantRequest request);

    void softDeleteVariant(Long id);

    void restoreVariant(Long id);

    void handleBulkAction(VariantBulkActionRequest request);

}
