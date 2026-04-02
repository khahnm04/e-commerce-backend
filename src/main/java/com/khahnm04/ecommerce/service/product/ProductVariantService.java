package com.khahnm04.ecommerce.service.product;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.product.ProductVariantFilterRequest;
import com.khahnm04.ecommerce.dto.request.product.ProductVariantRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.product.ProductVariantResponse;
import com.khahnm04.ecommerce.entity.product.ProductVariant;

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
