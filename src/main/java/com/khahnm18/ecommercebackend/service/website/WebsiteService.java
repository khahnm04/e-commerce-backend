package com.khahnm18.ecommercebackend.service.website;

import com.khahnm18.ecommercebackend.dto.request.website.WebsiteRequest;
import com.khahnm18.ecommercebackend.dto.response.website.WebsiteResponse;

import java.util.List;

public interface WebsiteService {

    WebsiteResponse getWebsiteInfo();

    List<WebsiteResponse> getWebsiteInfoHistory();

    WebsiteResponse updateWebsiteInfo(WebsiteRequest request);

}
