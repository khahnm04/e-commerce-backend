package com.khahnm04.ecommerce.service.auth.impl;

import com.khahnm04.ecommerce.dto.projection.auth.RoleProjection;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.auth.RoleFilterRequest;
import com.khahnm04.ecommerce.dto.request.auth.RoleRequest;
import com.khahnm04.ecommerce.dto.response.auth.PermissionResponse;
import com.khahnm04.ecommerce.dto.response.auth.RoleResponse;
import com.khahnm04.ecommerce.entity.auth.Permission;
import com.khahnm04.ecommerce.entity.auth.Role;
import com.khahnm04.ecommerce.entity.auth.RolePermission;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.auth.PermissionMapper;
import com.khahnm04.ecommerce.repository.auth.PermissionRepository;
import com.khahnm04.ecommerce.repository.auth.RolePermissionRepository;
import com.khahnm04.ecommerce.repository.auth.RoleRepository;
import com.khahnm04.ecommerce.service.auth.PermissionCacheService;
import com.khahnm04.ecommerce.service.auth.RoleService;
import com.khahnm04.ecommerce.mapper.auth.RoleMapper;
import com.khahnm04.ecommerce.specification.auth.RoleSpecification;
import com.khahnm04.ecommerce.util.PaginationUtil;
import com.khahnm04.ecommerce.util.SecurityUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.stream.Collectors;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Slf4j(topic = "ROLE-SERVICE")
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private final PermissionCacheService permissionCacheService;

    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest request) {
        if (roleRepository.existsByCodeAndIsDeletedFalse(request.code())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
        Role role = roleMapper.toRole(request);
        role = roleRepository.save(role);
        List<Permission> permissions = assignPermissionsToRole(role, request.permissions());

        Set<PermissionResponse> permissionResponses = permissions.stream()
            .map(permissionMapper::toPermissionResponse)
            .collect(Collectors.toSet());

        log.info("Role created: code={}, name={}", role.getCode(), role.getName());
        return roleMapper.toRoleResponse(role, permissionResponses);
    }

    @Override
    public RoleResponse getRoleDetailById(Long id, boolean isDeleted) {
        Role role = isDeleted
            ? getSoftDeletedRoleById(id)
            : getRoleById(id);

        Set<PermissionResponse> permissions = rolePermissionRepository.findPermissionsByRoleId(role.getId())
            .stream()
            .map(permissionMapper::toPermissionResponse)
            .collect(Collectors.toSet());

        return roleMapper.toRoleResponse(role, permissions);
    }

    @Override
    public Page<RoleResponse> getAllRoles(RoleFilterRequest request) {
        Pageable pageable = PaginationUtil.createPageable(
            request.page(),
            request.size(),
            request.sort()
        );
        Page<RoleProjection> rolePage = roleRepository.findBy(
            RoleSpecification.buildQuery(request, request.isDeleted()),
            query -> query.as(RoleProjection.class).page(pageable)
        );
        return rolePage.map(roleMapper::toRoleResponse);
    }

    @Override
    @Transactional
    public RoleResponse updateRole(Long id, RoleRequest request) {
        if (roleRepository.existsByCodeAndIdNotAndIsDeletedFalse(request.code(), id)) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
        Role role = getRoleById(id);
        roleMapper.updateRole(role, request);
        List<Permission> permissions = replacePermissionsForRole(role, request.permissions());

        Set<PermissionResponse> permissionResponses = permissions.stream()
            .map(permissionMapper::toPermissionResponse)
            .collect(Collectors.toSet());

        log.info("Role updated: code={}, name={}", role.getCode(), role.getName());
        return roleMapper.toRoleResponse(role, permissionResponses);
    }

    @Override
    @Transactional
    public void softDeleteRole(Long id) {
        Role role = getRoleById(id);
        role.setIsDeleted(true);
        role.setDeletedAt(Instant.now());
        role.setDeletedBy(SecurityUtil.getCurrentUser());
        roleRepository.save(role);
        permissionCacheService.evictAll();
        log.info("Soft deleted role with id: {}", id);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        Role role = getSoftDeletedRoleById(id);
        rolePermissionRepository.deleteByRoleId(id);
        roleRepository.delete(role);
        permissionCacheService.evictAll();
        log.info("Deleted role with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreRole(Long id) {
        Role role = getSoftDeletedRoleById(id);
        role.setIsDeleted(false);
        role.setDeletedAt(null);
        role.setDeletedBy(null);
        roleRepository.save(role);
        permissionCacheService.evictAll();
        log.info("Role restored with id: {}", id);
    }

    @Override
    @Transactional
    public void bulkAction(BulkActionRequest request) {
        Set<Long> roleIds = request.ids();
        String action = request.action().toUpperCase();
        switch (action) {
            case "SOFT_DELETE" -> {
                roleRepository.softDeleteAllByIds(roleIds, SecurityUtil.getCurrentUser());
                permissionCacheService.evictAll();
                log.info("Bulk action soft delete roles ids: {}", roleIds);
            }
            case "RESTORE" -> {
                roleRepository.restoreAllByIds(roleIds);
                permissionCacheService.evictAll();
                log.info("Bulk action restore roles ids: {}", roleIds);
            }
            case "DELETE" -> {
                rolePermissionRepository.deleteAllByRoleIds(roleIds);
                roleRepository.deleteAllByIds(roleIds);
                permissionCacheService.evictAll();
                log.info("Bulk action delete roles ids: {}", roleIds);
            }
        }
    }

    private Role getRoleById(Long id) {
        return roleRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
    }

    private Role getSoftDeletedRoleById(Long id) {
        return roleRepository.findByIdAndIsDeletedTrue(id)
            .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
    }

    private List<Permission> assignPermissionsToRole(Role role, Set<Long> permissionIds) {
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        if (permissions.size() != permissionIds.size()) {
            throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);
        }

        List<RolePermission> rolePermissions = permissions.stream()
            .map(permission -> RolePermission.builder()
                .roleId(role.getId())
                .permissionId(permission.getId())
                .build())
            .collect(Collectors.toList());

        rolePermissionRepository.saveAll(rolePermissions);
        log.info("Assigned {} permissions to roleCode={}", rolePermissions.size(), role.getCode());
        return permissions;
    }

    private List<Permission> replacePermissionsForRole(Role role, Set<Long> permissionIds) {
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        if (permissions.size() != permissionIds.size()) {
            throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);
        }
        List<RolePermission> existingRolPermissions = rolePermissionRepository.findByRoleId(role.getId());
        Set<Long> existingPermissionIds = existingRolPermissions.stream()
            .map(RolePermission::getPermissionId)
            .collect(Collectors.toSet());

        if (existingPermissionIds.equals(permissionIds)) {
            return permissions;
        }

        List<Long> toDeletePermissionIds = existingRolPermissions.stream()
            .filter(rp -> !permissionIds.contains(rp.getPermissionId()))
            .map(RolePermission::getId)
            .toList();

        List<Long> toAddPermissionIds = permissionIds.stream()
            .filter(permId -> !existingPermissionIds.contains(permId))
            .toList();

        if (!toDeletePermissionIds.isEmpty()) {
            rolePermissionRepository.deleteAllByIds(toDeletePermissionIds);
        }
        if (!toAddPermissionIds.isEmpty()) {
            rolePermissionRepository.insertAll(role.getId(), toAddPermissionIds);
        }
        permissionCacheService.evictAll();
        return permissions;
    }

}
