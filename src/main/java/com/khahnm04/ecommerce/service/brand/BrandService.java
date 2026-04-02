package com.khahnm04.ecommerce.service.brand;


import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.brand.BrandFilterRequest;
import com.khahnm04.ecommerce.dto.request.brand.BrandRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.brand.BrandResponse;
import com.khahnm04.ecommerce.entity.brand.Brand;

public interface BrandService {

    BrandResponse createBrand(BrandRequest request);

    Brand getBrandById(Long id);

    BrandResponse getBrandDetail(String identifier, boolean deleted);

    PageResponse<BrandResponse> getAllBrands(BrandFilterRequest filter);

    PageResponse<BrandResponse> getAllDeletedBrands(BrandFilterRequest filter);

    BrandResponse updateBrand(Long id, BrandRequest request);

    void softDeleteBrand(Long id);

    void restoreBrand(Long id);

    void handleBulkAction(BulkActionRequest request);

}
