package com.khahnm18.ecommercebackend.service.auth;

import com.khahnm18.ecommercebackend.dto.response.auth.PermissionResponse;
import java.util.List;

public interface PermissionService {

    List<PermissionResponse> getAllPermissions();

}
