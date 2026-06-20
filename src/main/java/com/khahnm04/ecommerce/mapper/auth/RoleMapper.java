package com.khahnm04.ecommerce.mapper.auth;

import com.khahnm04.ecommerce.dto.request.auth.RoleRequest;
import com.khahnm04.ecommerce.dto.response.auth.RoleResponse;
import com.khahnm04.ecommerce.dto.projection.auth.RoleProjection;
import com.khahnm04.ecommerce.dto.response.auth.PermissionResponse;
import com.khahnm04.ecommerce.entity.auth.Role;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class RoleMapper {

    public Role toRole(RoleRequest request) {
        return Role.builder()
            .code(request.code())
            .name(request.name())
            .build();
    }

    public RoleResponse toRoleResponse(Role role, Set<PermissionResponse> permissions) {
        RoleResponse.RoleResponseBuilder builder = RoleResponse.builder()
            .id(role.getId())
            .code(role.getCode())
            .name(role.getName())
            .createdAt(role.getCreatedAt())
            .createdBy(role.getCreatedBy())
            .updatedAt(role.getUpdatedAt())
            .updatedBy(role.getUpdatedBy())
            .deletedAt(role.getDeletedAt())
            .deletedBy(role.getDeletedBy())
            .isDeleted(role.getIsDeleted());
        if (permissions != null) {
            builder.permissions(permissions);
        }
        return builder.build();
    }

    public RoleResponse toRoleResponse(RoleProjection projection) {
        return RoleResponse.builder()
            .id(projection.getId())
            .code(projection.getCode())
            .name(projection.getName())
            .createdAt(projection.getCreatedAt())
            .createdBy(projection.getCreatedBy())
            .updatedAt(projection.getUpdatedAt())
            .updatedBy(projection.getUpdatedBy())
            .deletedAt(projection.getDeletedAt())
            .deletedBy(projection.getDeletedBy())
            .isDeleted(projection.getIsDeleted())
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
