package com.khahnm04.ecommerce.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.khahnm04.ecommerce.dto.request.SettingWebsiteRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.SettingWebsiteResponse;
import com.khahnm04.ecommerce.service.SettingWebsiteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/setting-websites")
@RequiredArgsConstructor
public class AdminSettingWebsiteController {

    private final SettingWebsiteService settingWebsiteService;

    // 1. cập nhật
    @PutMapping
    public ApiResponse<SettingWebsiteResponse> updateSetting(
            @Valid @RequestBody SettingWebsiteRequest request
    ) {
        return ApiResponse.<SettingWebsiteResponse>builder()
                .data(settingWebsiteService.updateSettingWebsite(request))
                .message("Update setting website successfully")
                .build();
    }

}
