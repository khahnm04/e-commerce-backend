package com.khahnm18.ecommercebackend.service.auth.impl;

import com.khahnm18.ecommercebackend.entity.auth.Permission;
import com.khahnm18.ecommercebackend.entity.auth.Role;
import com.khahnm18.ecommercebackend.entity.auth.RolePermission;
import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.repository.auth.PermissionRepository;
import com.khahnm18.ecommercebackend.repository.auth.RolePermissionRepository;
import com.khahnm18.ecommercebackend.service.auth.RolePermissionService;
import com.khahnm18.ecommercebackend.util.SecurityUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
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
        List<Permission> permissions = getPermissionsByPermissionIds(permissionIds);
        this.saveRolePermissions(role, permissions);
        role.setUpdatedAt(Instant.now());
        role.setUpdatedBy(SecurityUtil.getCurrentAccount());
        return permissions;
    }

    @Override
    @Transactional
    public List<Permission> replacePermissionsForRole(Role role, List<UUID> newPermissionIds) {
        List<Permission> newPermissions = getPermissionsByPermissionIds(newPermissionIds);
        List<UUID> currentPermissionIds = permissionRepository.findPermissionsByRoleId(role.getId())
            .stream()
            .map(Permission::getId)
            .toList();

        // Remove
        List<UUID> permissionIdsToRemove = currentPermissionIds.stream()
            .filter(currentPermissionId -> !newPermissionIds.contains(currentPermissionId))
            .toList();
        if (!permissionIdsToRemove.isEmpty()) {
            rolePermissionRepository.deleteByRoleIdAndPermissionIdIn(role.getId(), permissionIdsToRemove);
        }

        // Add
        List<Permission> permissionsToAdd = newPermissions.stream()
            .filter(newPermission -> !currentPermissionIds.contains(newPermission.getId()))
            .toList();
        if (!permissionsToAdd.isEmpty()) {
            this.saveRolePermissions(role, permissionsToAdd);
        }

        role.setUpdatedAt(Instant.now());
        role.setUpdatedBy(SecurityUtil.getCurrentAccount());
        return newPermissions;
    }

    private List<Permission> getPermissionsByPermissionIds(List<UUID> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        if (permissions.size() != permissionIds.size()) {
            throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);
        }
        return permissions;
    }

    private void saveRolePermissions(Role role, List<Permission> permissions) {
        List<RolePermission> rolePermissions = permissions.stream()
            .map(permission -> RolePermission.builder()
                .roleId(role.getId())
                .permissionId(permission.getId())
                .build())
            .toList();
        rolePermissionRepository.saveAll(rolePermissions);
        log.info("Assigned {} permissions to role: {}", rolePermissions.size(), role.getCode());
    }

}
