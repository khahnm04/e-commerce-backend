package com.khahnm04.ecommerce.service.product;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.product.AttributeFilterRequest;
import com.khahnm04.ecommerce.dto.request.product.AttributeRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.product.AttributeResponse;
import com.khahnm04.ecommerce.entity.product.Attribute;

public interface AttributeService {

    AttributeResponse createAttribute(AttributeRequest request);

    Attribute getAttributeById(Long id);

    AttributeResponse getAttributeDetail(Long id, boolean deleted);

    PageResponse<AttributeResponse> getAllAttributes(AttributeFilterRequest filter);

    PageResponse<AttributeResponse> getAllDeletedAttributes(AttributeFilterRequest filter);

    AttributeResponse updateAttribute(Long id, AttributeRequest request);

    void softDeleteAttribute(Long id);

    void restoreAttribute(Long id);

    void handleBulkAction(BulkActionRequest request);

}
