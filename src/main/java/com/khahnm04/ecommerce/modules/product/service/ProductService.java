package com.khahnm04.ecommerce.modules.product.service;

import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.modules.product.dto.request.ProductFilterRequest;
import com.khahnm04.ecommerce.modules.product.dto.request.ProductRequest;
import com.khahnm04.ecommerce.modules.product.dto.response.ProductResponse;
import com.khahnm04.ecommerce.modules.product.entity.Product;
import com.khahnm04.ecommerce.modules.product.enums.ProductStatus;
import java.util.Set;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    Product getProductById(Long id);

    ProductResponse getProductDetail(String identifier, boolean deleted);

    PageResponse<ProductResponse> getAllProducts(ProductFilterRequest filter);

    PageResponse<ProductResponse> getAllDeletedProducts(ProductFilterRequest filter);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void updateProductStatus(Long id, ProductStatus status);

    void softDeleteProduct(Long id);

    void restoreProduct(Long id);

    void handleBulkAction(Set<Long> ids, String action);

}
