package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.BrandFilterRequest;
import com.khahnm04.ecommerce.dto.request.BrandRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.BrandResponse;
import com.khahnm04.ecommerce.entity.Brand;

public interface BrandService {

    BrandResponse createBrand(BrandRequest request);

    Brand getBrandById(Long id);

    BrandResponse getBrandDetail(String identifier, boolean deleted);

    PageResponse<BrandResponse> getAllBrands(BrandFilterRequest filter);

    PageResponse<BrandResponse> getAllDeletedBrands(BrandFilterRequest filter);

    BrandResponse updateBrand(Long id, BrandRequest request);

    void softDeleteBrand(Long id);

    void deleteBrand(Long id);

    void restoreBrand(Long id);

    void handleBulkAction(BulkActionRequest request);

}
