package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.ProductVariantFilterRequest;
import com.khahnm04.ecommerce.dto.request.ProductVariantRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.ProductVariantResponse;
import com.khahnm04.ecommerce.entity.ProductVariant;

public interface ProductVariantService {

    ProductVariantResponse createProductVariant(ProductVariantRequest request);

    ProductVariant getProductVariantById(Long id);

    ProductVariantResponse getProductVariantDetail(String identifier, boolean deleted);

    PageResponse<ProductVariantResponse> getAllProductVariants(ProductVariantFilterRequest filter);

    PageResponse<ProductVariantResponse> getAllDeletedProductVariants(ProductVariantFilterRequest filter);

    ProductVariantResponse updateProductVariant(Long id, ProductVariantRequest request);

    void softDeleteProductVariant(Long id);

    void restoreProductVariant(Long id);

    void handleBulkAction(BulkActionRequest request);

}
