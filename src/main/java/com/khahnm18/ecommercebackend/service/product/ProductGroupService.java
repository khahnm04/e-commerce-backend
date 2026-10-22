package com.khahnm18.ecommercebackend.service.product;

import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.product.ProductGroupRequest;
import com.khahnm18.ecommercebackend.dto.request.product.ProductGroupSearchRequest;
import com.khahnm18.ecommercebackend.dto.response.product.ProductGroupResponse;
import org.springframework.data.domain.Page;
import java.time.Instant;
import java.util.UUID;

public interface ProductGroupService {

    ProductGroupResponse getProductGroupDetailById(UUID productGroupId, boolean isDeleted);

    Page<ProductGroupResponse> getAllProductGroups(
        int page,
        int size,
        ProductGroupSearchRequest request
    );

    ProductGroupResponse createProductGroup(ProductGroupRequest request);

    ProductGroupResponse updateProductGroup(UUID productGroupId, ProductGroupRequest request);

    void softDeleteProductGroup(UUID productGroupId);

    void restoreProductGroup(UUID productGroupId);

    void bulkAction(BulkActionRequest request);

    void deleteProductGroup(UUID productGroupId);

}

