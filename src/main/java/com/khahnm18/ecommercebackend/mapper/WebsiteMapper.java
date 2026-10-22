package com.khahnm18.ecommercebackend.mapper;

import com.khahnm18.ecommercebackend.dto.request.website.WebsiteRequest;
import com.khahnm18.ecommercebackend.dto.response.website.WebsiteResponse;
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

    WebsiteInfo toWebsiteInfo(WebsiteRequest request);

    WebsiteResponse toResponse(WebsiteInfo websiteInfo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateWebsiteInfo(@MappingTarget WebsiteInfo websiteInfo, WebsiteRequest request);

}
