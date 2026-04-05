package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.response.PermissionResponse;
import java.util.List;

public interface PermissionService {

    List<PermissionResponse> getAllPermissions();

}
