package com.khahnm04.ecommerce.service.product;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.product.ProductFilterRequest;
import com.khahnm04.ecommerce.dto.request.product.ProductRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.product.ProductResponse;
import com.khahnm04.ecommerce.entity.product.Product;

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
