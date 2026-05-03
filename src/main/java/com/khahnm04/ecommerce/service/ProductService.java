package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.ProductFilterRequest;
import com.khahnm04.ecommerce.dto.request.filter.ProductVariantFilterRequest;
import com.khahnm04.ecommerce.dto.request.ProductRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.ProductResponse;
import com.khahnm04.ecommerce.dto.response.ProductVariantResponse;
import com.khahnm04.ecommerce.entity.Product;
import com.khahnm04.ecommerce.entity.ProductVariant;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    Product getProductById(Long id);

    ProductResponse getProductDetail(String identifier, boolean deleted);

    PageResponse<ProductResponse> getAllProducts(ProductFilterRequest filter);

    PageResponse<ProductResponse> getAllDeletedProducts(ProductFilterRequest filter);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void softDeleteProduct(Long id);

    void deleteProduct(Long id);

    void restoreProduct(Long id);

    void handleBulkAction(BulkActionRequest request);

    // Variant methods
    PageResponse<ProductVariantResponse> getAllProductVariants(ProductVariantFilterRequest filter);

    PageResponse<ProductVariantResponse> getAllDeletedProductVariants(ProductVariantFilterRequest filter);

    void softDeleteProductVariant(Long id);

    void restoreProductVariant(Long id);

    ProductVariant getProductVariantById(Long id);

}
