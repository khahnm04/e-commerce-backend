package com.khahnm18.ecommercebackend.service.website;

import com.khahnm18.ecommercebackend.dto.request.website.WebsiteInfoRequest;
import com.khahnm18.ecommercebackend.dto.response.response.WebsiteInfoResponse;
import java.util.List;

public interface WebsiteInfoService {

    WebsiteInfoResponse getWebsiteInfo();

    List<WebsiteInfoResponse> getWebsiteInfoHistory();

    WebsiteInfoResponse updateWebsiteInfo(WebsiteInfoRequest request);

}
