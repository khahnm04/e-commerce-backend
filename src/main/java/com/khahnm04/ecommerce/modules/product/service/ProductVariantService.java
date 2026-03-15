package com.khahnm04.ecommerce.modules.product.service;

import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.modules.product.dto.request.ProductVariantFilterRequest;
import com.khahnm04.ecommerce.modules.product.dto.request.ProductVariantRequest;
import com.khahnm04.ecommerce.modules.product.dto.response.ProductVariantResponse;
import com.khahnm04.ecommerce.modules.product.entity.ProductVariant;
import com.khahnm04.ecommerce.modules.product.enums.ProductVariantStatus;
import java.util.Set;

public interface ProductVariantService {

    ProductVariantResponse createProductVariant(ProductVariantRequest request);

    ProductVariant getProductVariantById(Long id);

    ProductVariantResponse getProductVariantDetail(String identifier, boolean deleted);

    PageResponse<ProductVariantResponse> getAllProductVariants(ProductVariantFilterRequest filter);

    PageResponse<ProductVariantResponse> getAllDeletedProductVariants(ProductVariantFilterRequest filter);

    ProductVariantResponse updateProductVariant(Long id, ProductVariantRequest request);

    void updateProductVariantStatus(Long id, ProductVariantStatus status);

    void softDeleteProductVariant(Long id);

    void restoreProductVariant(Long id);

    void handleBulkAction(Set<Long> productVariantIds, String action);

}
