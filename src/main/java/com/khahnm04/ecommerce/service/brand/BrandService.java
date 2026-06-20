package com.khahnm04.ecommerce.service.brand;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.brand.BrandFilterRequest;
import com.khahnm04.ecommerce.dto.request.brand.BrandRequest;
import com.khahnm04.ecommerce.dto.response.brand.BrandResponse;
import org.springframework.data.domain.Page;

public interface BrandService {

    BrandResponse createBrand(BrandRequest request);

    BrandResponse getBrandDetailById(Long id, boolean isDeleted);

    Page<BrandResponse> getAllBrands(BrandFilterRequest filter);

    BrandResponse updateBrand(Long id, BrandRequest request);

    void softDeleteBrand(Long id);

    void deleteBrand(Long id);

    void restoreBrand(Long id);

    void bulkAction(BulkActionRequest request);

}
