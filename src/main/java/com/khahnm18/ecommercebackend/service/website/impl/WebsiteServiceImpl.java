package com.khahnm18.ecommercebackend.service.website.impl;

import com.khahnm18.ecommercebackend.dto.response.website.WebsiteResponse;
import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.entity.website.WebsiteInfo;
import com.khahnm18.ecommercebackend.dto.request.website.WebsiteRequest;
import com.khahnm18.ecommercebackend.mapper.WebsiteMapper;
import com.khahnm18.ecommercebackend.service.website.WebsiteService;
import com.khahnm18.ecommercebackend.repository.website.WebsiteRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j(topic = "WEBSITE-INFO-SERVICE")
@Service
@RequiredArgsConstructor
public class WebsiteServiceImpl implements WebsiteService {

    private final WebsiteMapper websiteMapper;
    private final WebsiteRepository websiteRepository;

    @Override
    public WebsiteResponse getWebsiteInfo() {
        return websiteMapper.toResponse(getLatest());
    }

    @Override
    public List<WebsiteResponse> getWebsiteInfoHistory() {
        return websiteRepository.findAll(Sort.by("createdAt").descending())
            .stream()
            .map(websiteMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional
    public WebsiteResponse updateWebsiteInfo(WebsiteRequest request) {
        WebsiteInfo websiteInfo = getLatest();
        websiteMapper.updateWebsiteInfo(websiteInfo, request);
        log.info("Website info updated, new version id={}, name={}", websiteInfo.getId(), websiteInfo.getName());
        return websiteMapper.toResponse(websiteInfo);
    }

    private WebsiteInfo getLatest() {
        return websiteRepository.findFirstByOrderByCreatedAtDesc()
            .orElseThrow(() -> new AppException(ErrorCode.WEBSITE_INFO_NOT_FOUND));
    }

}
