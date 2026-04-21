package com.khahnm04.ecommerce.mapper;

import com.khahnm04.ecommerce.dto.request.SettingWebsiteRequest;
import com.khahnm04.ecommerce.dto.response.SettingWebsiteResponse;
import com.khahnm04.ecommerce.entity.SettingWebsite;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SettingWebsiteMapper {

    SettingWebsiteResponse toResponse(SettingWebsite settingWebsite);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSettingWebsite(@MappingTarget SettingWebsite settingWebsite, SettingWebsiteRequest request);
    
}
