package com.khahnm18.ecommercebackend.mapper;

import com.khahnm18.ecommercebackend.dto.response.auth.PermissionResponse;
import com.khahnm18.ecommercebackend.entity.auth.Permission;
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
