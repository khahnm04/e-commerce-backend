package com.khahnm18.ecommercebackend.service.website.impl;

import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.entity.website.WebsiteInfo;
import com.khahnm18.ecommercebackend.dto.request.website.WebsiteInfoRequest;
import com.khahnm18.ecommercebackend.dto.response.response.WebsiteInfoResponse;
import com.khahnm18.ecommercebackend.mapper.WebsiteInfoMapper;
import com.khahnm18.ecommercebackend.service.website.WebsiteInfoService;
import com.khahnm18.ecommercebackend.repository.website.WebsiteInfoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j(topic = "WEBSITE-INFO-SERVICE")
@Service
@RequiredArgsConstructor
public class WebsiteInfoServiceImpl implements WebsiteInfoService {

    private final WebsiteInfoMapper websiteInfoMapper;
    private final WebsiteInfoRepository websiteInfoRepository;

    @Override
    public WebsiteInfoResponse getWebsiteInfo() {
        return websiteInfoMapper.toResponse(getLatest());
    }

    @Override
    public List<WebsiteInfoResponse> getWebsiteInfoHistory() {
        return websiteInfoRepository.findAll(Sort.by("createdAt").descending())
            .stream()
            .map(websiteInfoMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional
    public WebsiteInfoResponse updateWebsiteInfo(WebsiteInfoRequest request) {
        WebsiteInfo websiteInfo = getLatest();
        websiteInfoMapper.updateWebsiteInfo(websiteInfo, request);
        log.info("Website info updated, new version id={}, name={}", websiteInfo.getId(), websiteInfo.getName());
        return websiteInfoMapper.toResponse(websiteInfo);
    }

    private WebsiteInfo getLatest() {
        return websiteInfoRepository.findFirstByOrderByCreatedAtDesc()
            .orElseThrow(() -> new AppException(ErrorCode.WEBSITE_INFO_NOT_FOUND));
    }

}
