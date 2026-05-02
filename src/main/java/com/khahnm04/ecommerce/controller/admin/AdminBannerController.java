package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.BannerFilterRequest;
import com.khahnm04.ecommerce.dto.request.BannerRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.BannerResponse;
import com.khahnm04.ecommerce.service.BannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/banners")
@RequiredArgsConstructor
public class AdminBannerController {

    private final BannerService bannerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<BannerResponse> createBanner(
            @Valid @RequestBody BannerRequest request
    ) {
        return ApiResponse.<BannerResponse>builder()
                .data(bannerService.createBanner(request))
                .message("Banner created successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BannerResponse> getBannerDetail(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean deleted
    ) {
        return ApiResponse.<BannerResponse>builder()
                .data(bannerService.getBannerDetail(id, deleted))
                .message("Get banner detail successfully")
                .build();
    }

    @GetMapping("/deleted")
    public ApiResponse<List<BannerResponse>> getAllDeletedBanners(
            @Valid @ModelAttribute BannerFilterRequest filter
    ) {
        PageResponse<BannerResponse> pageResponse = bannerService.getAllDeletedBanners(filter);
        return ApiResponse.<List<BannerResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get deleted banners successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<BannerResponse> updateBanner(
            @PathVariable Long id,
            @Valid @RequestBody BannerRequest request
    ) {
        return ApiResponse.<BannerResponse>builder()
                .data(bannerService.updateBanner(id, request))
                .message("Banner updated successfully")
                .build();
    }

    @PatchMapping("/{id}/soft-delete")
    public ApiResponse<Void> softDeleteBanner(
            @PathVariable Long id
    ) {
        bannerService.softDeleteBanner(id);
        return ApiResponse.<Void>builder()
                .message("Banner soft deleted successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBanner(
            @PathVariable Long id
    ) {
        bannerService.deleteBanner(id);
        return ApiResponse.<Void>builder()
                .message("Banner deleted successfully")
                .build();
    }

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restoreBanner(
            @PathVariable Long id
    ) {
        bannerService.restoreBanner(id);
        return ApiResponse.<Void>builder()
                .message("Banner restored successfully")
                .build();
    }

    @PatchMapping("/bulk")
    public ApiResponse<Void> handleBulkAction(
            @RequestBody BulkActionRequest request
    ) {
        bannerService.handleBulkAction(request);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
