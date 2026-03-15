package com.khahnm04.ecommerce.modules.catalog.service;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.catalog.dto.request.BrandBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.BrandFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.BrandRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.BrandResponse;
import com.khahnm04.ecommerce.modules.catalog.entity.Brand;

public interface BrandService {

    Brand getBrandById(Long id);

    BrandResponse getBrandDetail(String identifier, boolean deleted);

    BrandResponse createBrand(BrandRequest request);

    PageResponse<BrandResponse> getAllBrands(BrandFilterRequest filter);

    PageResponse<BrandResponse> getAllDeletedBrands(BrandFilterRequest filter);

    BrandResponse updateBrand(Long id, BrandRequest request);

    void softDeleteBrand(Long id);

    void restoreBrand(Long id);

    void handleBulkAction(BrandBulkActionRequest request);

}
