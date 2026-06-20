package com.khahnm04.ecommerce.mapper.auth;

import com.khahnm04.ecommerce.dto.response.auth.PermissionResponse;
import com.khahnm04.ecommerce.entity.auth.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {

    public PermissionResponse toPermissionResponse(Permission permission) {
        return PermissionResponse.builder()
            .id(permission.getId())
            .code(permission.getCode())
            .name(permission.getName())
            .build();
    }

}
