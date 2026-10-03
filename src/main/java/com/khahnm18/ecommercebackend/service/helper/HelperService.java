package com.khahnm18.ecommercebackend.service.helper;

import com.khahnm18.ecommercebackend.dto.request.helper.GenerateSlugRequest;
import com.khahnm18.ecommercebackend.dto.response.helper.GenerateSlugResponse;

public interface HelperService {

    GenerateSlugResponse generateSlug(GenerateSlugRequest request);

}
