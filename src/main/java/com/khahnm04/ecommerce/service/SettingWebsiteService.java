package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.request.SettingWebsiteRequest;
import com.khahnm04.ecommerce.dto.response.SettingWebsiteResponse;

public interface SettingWebsiteService {

    SettingWebsiteResponse getSettingWebsite();

    SettingWebsiteResponse updateSettingWebsite(SettingWebsiteRequest request);
    
}
