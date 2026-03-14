package com.khahnm04.ecommerce.modules.product.service;

import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.modules.product.dto.request.VariantValueFilterRequest;
import com.khahnm04.ecommerce.modules.product.dto.request.VariantValueRequest;
import com.khahnm04.ecommerce.modules.product.dto.response.VariantValueResponse;
import com.khahnm04.ecommerce.modules.product.entity.VariantValue;
import java.util.Set;

public interface VariantValueService {

    VariantValueResponse createVariantValue(VariantValueRequest request);

    VariantValue getVariantValueById(Long id);

    VariantValueResponse getVariantValueDetail(Long id, boolean deleted);

    PageResponse<VariantValueResponse> getAllVariantValues(VariantValueFilterRequest request);

    PageResponse<VariantValueResponse> getAllDeletedVariantValues(VariantValueFilterRequest request);

    VariantValueResponse updateVariantValue(Long id, VariantValueRequest request);

    void softDeleteVariantValue(Long id);

    void restoreVariantValue(Long id);

    void handleBulkAction(Set<Long> variantValueIds, String action);

}
