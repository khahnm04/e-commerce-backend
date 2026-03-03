package com.khahnm04.ecommerce.modules.catalog.service;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.catalog.dto.request.BrandBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.BrandFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.BrandRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.BrandUpdateStatusRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.BrandResponse;
import com.khahnm04.ecommerce.modules.catalog.entity.Brand;

public interface BrandService {

    BrandResponse createBrand(BrandRequest request);

    PageResponse<BrandResponse> getAllBrands(BrandFilterRequest filter);

    PageResponse<BrandResponse> getAllDeletedBrands(BrandFilterRequest filter);

    Brand getBrandById(Long id);

    BrandResponse getBrandDetailById(Long id);

    BrandResponse getBrandDetailBySlug(String slug);

    BrandResponse getDeletedBrandDetailById(Long id);

    BrandResponse updateBrand(Long id, BrandRequest request);

    void updateBrandStatus(Long id, BrandUpdateStatusRequest status);

    void softDeleteBrand(Long id);

    void restoreBrand(Long id);

    void handleBulkAction(BrandBulkActionRequest request);

}
