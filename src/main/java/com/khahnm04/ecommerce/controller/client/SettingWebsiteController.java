package com.khahnm04.ecommerce.controller.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.SettingWebsiteResponse;
import com.khahnm04.ecommerce.service.SettingWebsiteService;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/setting-websites")
@RequiredArgsConstructor
public class SettingWebsiteController {

    private final SettingWebsiteService settingWebsiteService;

    // 2. get all
    @GetMapping
    public ApiResponse<SettingWebsiteResponse> getSetting() {
        return ApiResponse.<SettingWebsiteResponse>builder()
                .data(settingWebsiteService.getSettingWebsite())
                .message("Get setting website successfully")
                .build();
    }

}
