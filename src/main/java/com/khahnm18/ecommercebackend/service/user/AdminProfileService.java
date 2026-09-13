package com.khahnm18.ecommercebackend.service.user;

import com.khahnm18.ecommercebackend.dto.request.user.AdminCreationRequest;
import com.khahnm18.ecommercebackend.dto.response.user.AdminCreationResponse;

public interface AdminProfileService {

    AdminCreationResponse createAdminPrrofile(AdminCreationRequest request);

}
