package com.khahnm04.ecommerce.controller.client;

import com.khahnm04.ecommerce.dto.request.filter.BannerFilterRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.BannerResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.service.BannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @GetMapping
    public ApiResponse<List<BannerResponse>> getAllBanners(
        @Valid @ModelAttribute BannerFilterRequest filter
    ) {
        PageResponse<BannerResponse> pageResponse = bannerService.getAllBanners(filter);
        return ApiResponse.<List<BannerResponse>>builder()
            .data(pageResponse.data())
            .meta(pageResponse.meta())
            .message("Get all banners successfully")
            .build();
    }

}
