package com.khahnm04.ecommerce.service.permission;

import com.khahnm04.ecommerce.dto.response.permission.PermissionResponse;
import java.util.List;

public interface PermissionService {

    List<PermissionResponse> getAllPermissions();

}
