package com.khahnm04.ecommerce.modules.product.service;

import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.modules.product.dto.request.VariantFilterRequest;
import com.khahnm04.ecommerce.modules.product.dto.request.VariantRequest;
import com.khahnm04.ecommerce.modules.product.dto.response.VariantResponse;
import com.khahnm04.ecommerce.modules.product.entity.Variant;
import java.util.Set;

public interface VariantService {

    VariantResponse createVariant(VariantRequest request);

    Variant getVariantById(Long id);

    VariantResponse getVariantDetail(Long id, boolean deleted);

    PageResponse<VariantResponse> getAllVariants(VariantFilterRequest request);

    PageResponse<VariantResponse> getAllDeletedVariants(VariantFilterRequest request);

    VariantResponse updateVariant(Long id, VariantRequest request);

    void softDeleteVariant(Long id);

    void restoreVariant(Long id);

    void handleBulkAction(Set<Long> variantIds, String action);

}
