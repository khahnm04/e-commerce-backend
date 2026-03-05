package com.khahnm04.ecommerce.modules.catalog.service;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductVariantBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductVariantFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductVariantRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductVariantUpdateStatusRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.ProductVariantResponse;
import com.khahnm04.ecommerce.modules.catalog.entity.ProductVariant;

public interface ProductVariantService {

    ProductVariantResponse createProductVariant(ProductVariantRequest request);

    ProductVariant getProductVariantById(Long id);

    ProductVariantResponse getProductVariantDetail(String identifier, boolean deleted);

    PageResponse<ProductVariantResponse> getAllProductVariants(ProductVariantFilterRequest filter);

    PageResponse<ProductVariantResponse> getAllDeletedProductVariants(ProductVariantFilterRequest filter);

    ProductVariantResponse updateProductVariant(Long id, ProductVariantRequest request);

    ProductVariantResponse updateProductVariantStatus(Long id, ProductVariantUpdateStatusRequest request);

    void softDeleteProductVariant(Long id);

    void restoreProductVariant(Long id);

    void handleBulkAction(ProductVariantBulkActionRequest request);

}
