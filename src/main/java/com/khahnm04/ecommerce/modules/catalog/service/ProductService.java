package com.khahnm04.ecommerce.modules.catalog.service;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductUpdateStatusRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.ProductResponse;
import com.khahnm04.ecommerce.modules.catalog.entity.Product;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    PageResponse<ProductResponse> getAllProducts(ProductFilterRequest filter);

    PageResponse<ProductResponse> getAllDeletedProducts(ProductFilterRequest filter);

    Product getProductById(Long id);

    ProductResponse getProductDetailById(Long id);

    ProductResponse getProductDetailBySlug(String slug);

    ProductResponse getDeletedProductDetailById(Long id);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void updateProductStatus(Long id, ProductUpdateStatusRequest status);

    void softDeleteProduct(Long id);

    void restoreProduct(Long id);

    void handleBulkAction(ProductBulkActionRequest request);

}
