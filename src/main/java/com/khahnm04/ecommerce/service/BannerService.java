package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.BannerFilterRequest;
import com.khahnm04.ecommerce.dto.request.BannerRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.BannerResponse;
import com.khahnm04.ecommerce.entity.Banner;

public interface BannerService {

    BannerResponse createBanner(BannerRequest request);

    Banner getBannerById(Long id);

    BannerResponse getBannerDetail(Long id, boolean deleted);

    PageResponse<BannerResponse> getAllBanners(BannerFilterRequest filter);

    PageResponse<BannerResponse> getAllDeletedBanners(BannerFilterRequest filter);

    BannerResponse updateBanner(Long id, BannerRequest request);

    void softDeleteBanner(Long id);

    void deleteBanner(Long id);

    void restoreBanner(Long id);

    void handleBulkAction(BulkActionRequest request);

}
