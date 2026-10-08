package com.khahnm18.ecommercebackend.mapper;

import com.khahnm18.ecommercebackend.dto.request.website.WebsiteInfoRequest;
import com.khahnm18.ecommercebackend.dto.response.website.WebsiteInfoResponse;
import com.khahnm18.ecommercebackend.entity.website.WebsiteInfo;
import org.springframework.stereotype.Component;

@Component
public class WebsiteMapper {

    public WebsiteInfo toWebsiteInfo(WebsiteInfoRequest request) {
        return WebsiteInfo.builder()
            .name(request.name())
            .phoneNumber(request.phoneNumber())
            .email(request.email())
            .address(request.address())
            .logo(request.logo())
            .favicon(request.favicon())
            .build();
    }

    public WebsiteInfoResponse toResponse(WebsiteInfo websiteInfo) {
        return WebsiteInfoResponse.builder()
            .id(websiteInfo.getId())
            .name(websiteInfo.getName())
            .phoneNumber(websiteInfo.getPhoneNumber())
            .email(websiteInfo.getEmail())
            .address(websiteInfo.getAddress())
            .logo(websiteInfo.getLogo())
            .favicon(websiteInfo.getFavicon())
            .createdAt(websiteInfo.getCreatedAt())
            .build();
    }

    public void updateWebsiteInfo(WebsiteInfo websiteInfo, WebsiteInfoRequest request) {
        if (request.name() != null) {
            websiteInfo.setName(request.name());
        }
        if (request.phoneNumber() != null) {
            websiteInfo.setPhoneNumber(request.phoneNumber());
        }
        if (request.email() != null) {
            websiteInfo.setEmail(request.email());
        }
        if (request.address() != null) {
            websiteInfo.setAddress(request.address());
        }
        if (request.logo() != null) {
            websiteInfo.setLogo(request.logo());
        }
        if (request.favicon() != null) {
            websiteInfo.setFavicon(request.favicon());
        }
    }

}
