package com.khahnm18.ecommercebackend.service.product;

import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.product.ProductVersionRequest;
import com.khahnm18.ecommercebackend.dto.response.product.ProductVersionResponse;
import org.springframework.data.domain.Page;
import java.time.Instant;
import java.util.UUID;

public interface ProductVersionService {

    ProductVersionResponse getProductVersionDetailById(UUID productVersionId, boolean isDeleted);

    Page<ProductVersionResponse> getAllProductVersions(
        int page,
        int size,
        String keyword,
        boolean isDeleted,
        Instant startDate,
        Instant endDate,
        UUID groupId
    );

    ProductVersionResponse createProductVersion(ProductVersionRequest request);

    ProductVersionResponse updateProductVersion(UUID productVersionId, ProductVersionRequest request);

    void softDeleteProductVersion(UUID productVersionId);

    void restoreProductVersion(UUID productVersionId);

    void bulkAction(BulkActionRequest request);

    void deleteProductVersion(UUID productVersionId);

}
