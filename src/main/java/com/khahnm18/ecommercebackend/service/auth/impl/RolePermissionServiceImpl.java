package com.khahnm18.ecommercebackend.service.auth.impl;

import com.khahnm18.ecommercebackend.entity.auth.Permission;
import com.khahnm18.ecommercebackend.entity.auth.Role;
import com.khahnm18.ecommercebackend.entity.auth.RolePermission;
import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.repository.auth.PermissionRepository;
import com.khahnm18.ecommercebackend.repository.auth.RolePermissionRepository;
import com.khahnm18.ecommercebackend.service.auth.RolePermissionService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j(topic = "ROLE-PERMISSION-SERVICE")
@Service
@RequiredArgsConstructor
public class RolePermissionServiceImpl implements RolePermissionService {

    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    @Transactional
    public List<Permission> assignPermissionsToRole(Role role, List<UUID> permissionIds) {
        List<Permission> permissions = validatePermissions(permissionIds);

        List<RolePermission> rolePermissions = permissions.stream()
            .map(permission -> RolePermission.builder()
                .roleId(role.getId())
                .permissionId(permission.getId())
                .build())
            .toList();

        rolePermissionRepository.saveAll(rolePermissions);
        log.info("Assigned {} permissions to role: {}", rolePermissions.size(), role.getCode());
        return permissions;
    }

    @Override
    @Transactional
    public List<Permission> replacePermissionsForRole(Role role, List<UUID> permissionIds) {
        List<Permission> permissions = validatePermissions(permissionIds);
        List<RolePermission> existingRolePermissions = rolePermissionRepository.findByRoleId(role.getId());

        List<UUID> existingPermissionIds = existingRolePermissions.stream()
            .map(RolePermission::getPermissionId)
            .toList();

        if (existingPermissionIds.equals(permissionIds)) {
            return permissions;
        }

        List<UUID> toRemove = existingRolePermissions.stream()
            .filter(rolePermission -> !permissionIds.contains(rolePermission.getPermissionId()))
            .map(RolePermission::getId)
            .toList();

        List<UUID> toAdd = permissionIds.stream()
            .filter(permissionId -> !existingPermissionIds.contains(permissionId))
            .toList();

        if (!toRemove.isEmpty()) {
            rolePermissionRepository.deleteAllByIds(toRemove);
        }
        if (!toAdd.isEmpty()) {
            rolePermissionRepository.insertAll(role.getId(), toAdd);
        }
        return permissions;
    }

    private List<Permission> validatePermissions(List<UUID> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        if (permissions.size() != permissionIds.size()) {
            throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);
        }
        return permissions;
    }

}
