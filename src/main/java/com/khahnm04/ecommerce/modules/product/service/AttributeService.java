package com.khahnm04.ecommerce.modules.product.service;

import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.modules.product.dto.request.AttributeFilterRequest;
import com.khahnm04.ecommerce.modules.product.dto.request.AttributeRequest;
import com.khahnm04.ecommerce.modules.product.dto.response.AttributeResponse;
import com.khahnm04.ecommerce.modules.product.entity.Attribute;
import java.util.Set;

public interface AttributeService {

    AttributeResponse createAttribute(AttributeRequest request);

    Attribute getAttributeById(Long id);

    AttributeResponse getAttributeDetail(Long id, boolean deleted);

    PageResponse<AttributeResponse> getAllAttributes(AttributeFilterRequest filter);

    PageResponse<AttributeResponse> getAllDeletedAttributes(AttributeFilterRequest filter);

    AttributeResponse updateAttribute(Long id, AttributeRequest request);

    void softDeleteAttribute(Long id);

    void restoreAttribute(Long id);

    void handleBulkAction(Set<Long> ids, String action);

}
