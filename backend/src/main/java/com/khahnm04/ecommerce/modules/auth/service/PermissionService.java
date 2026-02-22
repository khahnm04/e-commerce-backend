package com.khahnm04.ecommerce.modules.auth.service;

import com.khahnm04.ecommerce.modules.auth.dto.response.PermissionResponse;
import java.util.List;

public interface PermissionService {

    List<PermissionResponse> getAllPermissions();

}
