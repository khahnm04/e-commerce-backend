package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.VariantFilterRequest;
import com.khahnm04.ecommerce.dto.request.VariantRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.VariantResponse;
import com.khahnm04.ecommerce.entity.Variant;

public interface VariantService {

    VariantResponse createVariant(VariantRequest request);

    Variant getVariantById(Long id);

    VariantResponse getVariantDetail(Long id, boolean deleted);

    PageResponse<VariantResponse> getAllVariants(VariantFilterRequest request);

    PageResponse<VariantResponse> getAllDeletedVariants(VariantFilterRequest request);

    VariantResponse updateVariant(Long id, VariantRequest request);

    void softDeleteVariant(Long id);

    void deleteVariant(Long id);

    void restoreVariant(Long id);

    void handleBulkAction(BulkActionRequest request);

}
