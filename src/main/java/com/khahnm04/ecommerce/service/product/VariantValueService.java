package com.khahnm04.ecommerce.service.product;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.product.VariantValueFilterRequest;
import com.khahnm04.ecommerce.dto.request.product.VariantValueRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.product.VariantValueResponse;
import com.khahnm04.ecommerce.entity.product.VariantValue;

public interface VariantValueService {

    VariantValueResponse createVariantValue(VariantValueRequest request);

    VariantValue getVariantValueById(Long id);

    VariantValueResponse getVariantValueDetail(Long id, boolean deleted);

    PageResponse<VariantValueResponse> getAllVariantValues(VariantValueFilterRequest request);

    PageResponse<VariantValueResponse> getAllDeletedVariantValues(VariantValueFilterRequest request);

    VariantValueResponse updateVariantValue(Long id, VariantValueRequest request);

    void softDeleteVariantValue(Long id);

    void restoreVariantValue(Long id);

    void handleBulkAction(BulkActionRequest request);

}
