package com.khahnm04.ecommerce.service.auth;

import com.khahnm04.ecommerce.dto.response.auth.PermissionResponse;
import java.util.List;

public interface PermissionService {

    List<PermissionResponse> getAllPermissions();

}
