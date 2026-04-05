package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.ProductFilterRequest;
import com.khahnm04.ecommerce.dto.request.ProductRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.ProductResponse;
import com.khahnm04.ecommerce.entity.Product;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    Product getProductById(Long id);

    ProductResponse getProductDetail(String identifier, boolean deleted);

    PageResponse<ProductResponse> getAllProducts(ProductFilterRequest filter);

    PageResponse<ProductResponse> getAllDeletedProducts(ProductFilterRequest filter);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void softDeleteProduct(Long id);

    void restoreProduct(Long id);

    void handleBulkAction(BulkActionRequest request);

}
