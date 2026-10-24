package com.khahnm18.ecommercebackend.service.product;

import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.product.BrandRequest;
import com.khahnm18.ecommercebackend.dto.request.product.BrandSearchRequest;
import com.khahnm18.ecommercebackend.dto.response.product.BrandResponse;
import org.springframework.data.domain.Page;
import java.util.UUID;

public interface BrandService {

    BrandResponse getBrandDetailById(UUID brandId, boolean isDeleted);

    Page<BrandResponse> getAllAndSearchBrands(int page, int size, BrandSearchRequest request);

    BrandResponse createBrand(BrandRequest request);

    BrandResponse updateBrand(UUID brandId, BrandRequest request);

    void softDeleteBrand(UUID brandId);

    void restoreBrand(UUID brandId);

    void bulkAction(BulkActionRequest request);

    void deleteBrand(UUID brandId);

}
