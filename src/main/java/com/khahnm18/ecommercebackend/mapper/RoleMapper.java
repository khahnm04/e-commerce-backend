package com.khahnm18.ecommercebackend.mapper;

import com.khahnm18.ecommercebackend.dto.request.auth.RoleRequest;
import com.khahnm18.ecommercebackend.dto.response.auth.PermissionResponse;
import com.khahnm18.ecommercebackend.dto.response.auth.RoleResponse;
import com.khahnm18.ecommercebackend.entity.auth.Role;
import com.khahnm18.ecommercebackend.entity.auth.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.Objects;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleMapper {

    private final PermissionMapper permissionMapper;

    public Role toRole(RoleRequest request) {
        if (request == null) return null;
        return Role.builder()
            .code(request.code())
            .name(request.name())
            .build();
    }

    public RoleResponse toRoleResponse(Role role) {
        if (role == null) return null;
        return RoleResponse.builder()
            .id(role.getId())
            .code(role.getCode())
            .name(role.getName())
            .createdBy(role.getCreatedBy())
            .createdAt(role.getCreatedAt())
            .updatedBy(role.getUpdatedBy())
            .updatedAt(role.getUpdatedAt())
            .deletedAt(role.getDeletedAt())
            .deletedBy(role.getDeletedBy())
            .isDeleted(role.getIsDeleted())
            .build();
    }

    public RoleResponse toRoleResponseWithPermissions(Role role, List<Permission> permissions) {
        if (role == null) return null;

        List<PermissionResponse> permissionResponses = (permissions == null)
            ? Collections.emptyList()
            : permissions.stream()
                .filter(Objects::nonNull)
                .map(permissionMapper::toPermissionResponse)
                .toList();

        return RoleResponse.builder()
            .id(role.getId())
            .code(role.getCode())
            .name(role.getName())
            .permissions(permissionResponses)
            .createdBy(role.getCreatedBy())
            .createdAt(role.getCreatedAt())
            .updatedBy(role.getUpdatedBy())
            .updatedAt(role.getUpdatedAt())
            .deletedAt(role.getDeletedAt())
            .deletedBy(role.getDeletedBy())
            .isDeleted(role.getIsDeleted())
            .build();
    }

    public void updateRole(Role role, RoleRequest request) {
        if (request.code() != null) {
            role.setCode(request.code());
        }
        if (request.name() != null) {
            role.setName(request.name());
        }
    }

}
