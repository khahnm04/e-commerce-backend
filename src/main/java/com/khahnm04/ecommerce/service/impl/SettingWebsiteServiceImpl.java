package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.dto.request.SettingWebsiteRequest;
import com.khahnm04.ecommerce.dto.response.SettingWebsiteResponse;
import com.khahnm04.ecommerce.entity.SettingWebsite;
import com.khahnm04.ecommerce.mapper.SettingWebsiteMapper;
import com.khahnm04.ecommerce.repository.SettingWebsiteRepository;
import com.khahnm04.ecommerce.service.SettingWebsiteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettingWebsiteServiceImpl implements SettingWebsiteService {

    private final SettingWebsiteRepository settingWebsiteRepository;
    private final SettingWebsiteMapper settingWebsiteMapper;

    @Override
    public SettingWebsiteResponse getSettingWebsite() {
        List<SettingWebsite> settings = settingWebsiteRepository.findAll();
        SettingWebsite setting = settings.isEmpty() ? new SettingWebsite() : settings.get(0);
        return settingWebsiteMapper.toResponse(setting);
    }

    @Override
    @Transactional
    public SettingWebsiteResponse updateSettingWebsite(SettingWebsiteRequest request) {
        List<SettingWebsite> settings = settingWebsiteRepository.findAll();
        SettingWebsite setting = settings.isEmpty() ? new SettingWebsite() : settings.get(0);
        
        settingWebsiteMapper.updateSettingWebsite(setting, request);
        SettingWebsite updatedSetting = settingWebsiteRepository.save(setting);
        
        log.info("Setting website updated successfully.");
        return settingWebsiteMapper.toResponse(updatedSetting);
    }
    
}
