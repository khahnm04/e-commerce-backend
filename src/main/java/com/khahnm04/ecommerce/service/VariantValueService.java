package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.VariantValueFilterRequest;
import com.khahnm04.ecommerce.dto.request.VariantValueRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.VariantValueResponse;
import com.khahnm04.ecommerce.entity.VariantValue;

public interface VariantValueService {

    VariantValueResponse createVariantValue(VariantValueRequest request);

    VariantValue getVariantValueById(Long id);

    VariantValueResponse getVariantValueDetail(Long id, boolean deleted);

    PageResponse<VariantValueResponse> getAllVariantValues(VariantValueFilterRequest request);

    PageResponse<VariantValueResponse> getAllDeletedVariantValues(VariantValueFilterRequest request);

    VariantValueResponse updateVariantValue(Long id, VariantValueRequest request);

    void softDeleteVariantValue(Long id);

    void deleteVariantValue(Long id);

    void restoreVariantValue(Long id);

    void handleBulkAction(BulkActionRequest request);

}
