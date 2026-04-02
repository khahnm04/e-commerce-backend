package com.khahnm04.ecommerce.service.product;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.product.VariantFilterRequest;
import com.khahnm04.ecommerce.dto.request.product.VariantRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.product.VariantResponse;
import com.khahnm04.ecommerce.entity.product.Variant;

public interface VariantService {

    VariantResponse createVariant(VariantRequest request);

    Variant getVariantById(Long id);

    VariantResponse getVariantDetail(Long id, boolean deleted);

    PageResponse<VariantResponse> getAllVariants(VariantFilterRequest request);

    PageResponse<VariantResponse> getAllDeletedVariants(VariantFilterRequest request);

    VariantResponse updateVariant(Long id, VariantRequest request);

    void softDeleteVariant(Long id);

    void restoreVariant(Long id);

    void handleBulkAction(BulkActionRequest request);

}
