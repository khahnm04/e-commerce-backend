package com.khahnm18.ecommercebackend.service.brand;

import com.khahnm18.ecommercebackend.enums.BrandStatus;
import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.brand.BrandRequest;
import com.khahnm18.ecommercebackend.dto.response.brand.BrandResponse;
import org.springframework.data.domain.Page;
import java.time.Instant;
import java.util.UUID;

public interface BrandService {

    BrandResponse getBrandDetailById(UUID brandId, boolean isDeleted);

    Page<BrandResponse> getAllBrands(
        int page,
        int size,
        String sort,
        String keyword,
        boolean isDeleted,
        Instant startDate,
        Instant endDate,
        BrandStatus status
    );

    BrandResponse createBrand(BrandRequest request);

    BrandResponse updateBrand(UUID brandId, BrandRequest request);

    void softDeleteBrand(UUID brandId);

    void restoreBrand(UUID brandId);

    void bulkAction(BulkActionRequest request);

    void deleteBrand(UUID brandId);

}
