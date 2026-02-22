package com.khahnm04.ecommerce.modules.catalog.service;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.catalog.dto.request.AttributeBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.AttributeFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.AttributeRequest;

import com.khahnm04.ecommerce.modules.catalog.dto.response.AttributeResponse;
import com.khahnm04.ecommerce.modules.catalog.entity.Attribute;

public interface AttributeService {

    AttributeResponse createAttribute(AttributeRequest request);

    PageResponse<AttributeResponse> getAllAttributes(AttributeFilterRequest filter);

    PageResponse<AttributeResponse> getAllDeletedAttributes(AttributeFilterRequest filter);

    Attribute getAttributeById(Long id);

    AttributeResponse getAttributeDetailById(Long id);

    AttributeResponse getAttributeDetailByCode(String code);

    AttributeResponse updateAttribute(Long id, AttributeRequest request);

    void softDeleteAttribute(Long id);

    void restoreAttribute(Long id);

    void handleBulkAction(AttributeBulkActionRequest request);

}
