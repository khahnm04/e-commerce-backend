package com.khahnm18.ecommercebackend.mapper;

import com.khahnm18.ecommercebackend.dto.request.website.WebsiteInfoRequest;
import com.khahnm18.ecommercebackend.dto.response.website.WebsiteInfoResponse;
import com.khahnm18.ecommercebackend.entity.website.WebsiteInfo;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface WebsiteMapper {

    WebsiteInfo toWebsiteInfo(WebsiteInfoRequest request);

    WebsiteInfoResponse toResponse(WebsiteInfo websiteInfo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateWebsiteInfo(@MappingTarget WebsiteInfo websiteInfo, WebsiteInfoRequest request);

}
