package com.khahnm04.ecommerce.modules.brand.service;

import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.modules.brand.dto.request.BrandFilterRequest;
import com.khahnm04.ecommerce.modules.brand.dto.request.BrandRequest;
import com.khahnm04.ecommerce.modules.brand.dto.response.BrandResponse;
import com.khahnm04.ecommerce.modules.brand.entity.Brand;
import com.khahnm04.ecommerce.modules.brand.enums.BrandStatus;
import java.util.Set;

public interface BrandService {

    BrandResponse createBrand(BrandRequest request);

    Brand getBrandById(Long id);

    BrandResponse getBrandDetail(String identifier, boolean deleted);

    PageResponse<BrandResponse> getAllBrands(BrandFilterRequest filter);

    PageResponse<BrandResponse> getAllDeletedBrands(BrandFilterRequest filter);

    BrandResponse updateBrand(Long id, BrandRequest request);

    void updateBrandStatus(Long id, BrandStatus status);

    void softDeleteBrand(Long id);

    void restoreBrand(Long id);

    void handleBulkAction(Set<Long> brandIds, String action);

}
