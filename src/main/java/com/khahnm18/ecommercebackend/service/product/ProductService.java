package com.khahnm18.ecommercebackend.service.product;

import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.product.ProductRequest;
import com.khahnm18.ecommercebackend.dto.response.product.ProductResponse;
import com.khahnm18.ecommercebackend.enums.ProductStatus;
import org.springframework.data.domain.Page;
import java.time.Instant;
import java.util.UUID;

public interface ProductService {

    ProductResponse getProductDetailById(UUID productId, boolean isDeleted);

    Page<ProductResponse> getAllProducts(
        int page,
        int size,
        String sort,
        String keyword,
        boolean isDeleted,
        Instant startDate,
        Instant endDate,
        ProductStatus status,
        UUID versionId
    );

    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(UUID productId, ProductRequest request);

    void softDeleteProduct(UUID productId);

    void restoreProduct(UUID productId);

    void bulkAction(BulkActionRequest request);

    void deleteProduct(UUID productId);

}
