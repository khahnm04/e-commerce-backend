package com.khahnm18.ecommercebackend.service.auth.impl;

import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.auth.RoleRequest;
import com.khahnm18.ecommercebackend.dto.request.auth.RoleSearchRequest;
import com.khahnm18.ecommercebackend.dto.response.auth.RoleResponse;
import com.khahnm18.ecommercebackend.entity.BaseEntity;
import com.khahnm18.ecommercebackend.entity.auth.Role;
import com.khahnm18.ecommercebackend.entity.auth.Permission;
import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.mapper.RoleMapper;
import com.khahnm18.ecommercebackend.repository.auth.PermissionRepository;
import com.khahnm18.ecommercebackend.service.auth.RolePermissionService;
import com.khahnm18.ecommercebackend.service.auth.RoleService;
import com.khahnm18.ecommercebackend.repository.auth.RoleRepository;
import com.khahnm18.ecommercebackend.service.auth.cache.PermissionCacheService;
import com.khahnm18.ecommercebackend.util.SecurityUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j(topic = "ROLE-SERVICE")
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionService rolePermissionService;
    private final PermissionCacheService permissionCacheService;

    @Override
    public RoleResponse getRoleDetailById(UUID id, boolean isDeleted) {
        Role role = isDeleted ? this.getSoftDeletedRoleById(id) : this.getRoleById(id);
        List<Permission> permissions = permissionRepository.findPermissionsByRoleId(id);
        return roleMapper.toRoleResponse(role, permissions);
    }

    @Override
    public Page<RoleResponse> getAllAndSearchRoles(int page, int size, RoleSearchRequest request) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size);
        return roleRepository
            .search(request, pageable)
            .map(roleMapper::toRoleResponse);
    }

    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest request) {
        Role role = roleMapper.toRole(request);
        role = this.saveRole(role);
        List<Permission> permissions = rolePermissionService.assignPermissionsToRole(role, request.permissions());
        log.info("Role created: code={}, name={}", role.getCode(), role.getName());
        return roleMapper.toRoleResponse(role, permissions);
    }

    @Override
    @Transactional
    public RoleResponse updateRole(UUID id, RoleRequest request) {
        Role role = getRoleById(id);
        roleMapper.updateRole(role, request);
        role = this.saveRole(role);
        List<Permission> permissions = rolePermissionService.replacePermissionsForRole(role, request.permissions());
        log.info("Role updated: code={}, name={}", role.getCode(), role.getName());
        return roleMapper.toRoleResponse(role, permissions);
    }

    @Override
    @Transactional
    public void softDeleteRole(UUID id) {
        Role role = getRoleById(id);
        this.softDelete(role);
        permissionCacheService.evictAll();
        log.info("Role soft deleted with id={}", id);
    }

    @Override
    @Transactional
    public void restoreRole(UUID id) {
        Role role = getSoftDeletedRoleById(id);
        this.restore(role);
        permissionCacheService.evictAll();
        log.info("Role restored with id={}", id);
    }

    @Override
    @Transactional
    public void bulkAction(BulkActionRequest request) {
        Set<UUID> roleIds = request.ids();
        String action = request.action().toUpperCase();
        List<Role> roles = roleRepository.findAllById(roleIds);
        switch (action) {
            case "SOFT_DELETE" -> {
                SecurityUtil.requireAuthority("ROLE_TRASH");
                roles.forEach(this::softDelete);
                permissionCacheService.evictAll();
                log.info("Bulk action soft delete roles with ids: {}", roleIds);
            }
            case "RESTORE" -> {
                SecurityUtil.requireAuthority("ROLE_TRASH");
                roles.forEach(this::restore);
                permissionCacheService.evictAll();
                log.info("Bulk action restore roles with ids: {}", roleIds);
            }
            case "DELETE" -> {
                SecurityUtil.requireAuthority("ROLE_DELETE");
                roleRepository.deleteAllInBatch(
                    roles.stream().filter(BaseEntity::getIsDeleted).toList()
                );
                permissionCacheService.evictAll();
                log.info("Bulk action delete roles with ids: {}", roleIds);
            }
        }
    }

    @Override
    @Transactional
    public void deleteRole(UUID id) {
        Role role = getSoftDeletedRoleById(id);
        roleRepository.delete(role);
        permissionCacheService.evictAll();
        log.info("Role deleted success with id: {}", id);
    }

    private Role saveRole(Role role) {
        try {
            return roleRepository.save(role);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Role save failed - duplicate entry: name={}", role.getName());
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
    }

    private Role getRoleById(UUID id) {
        return roleRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
    }

    private Role getSoftDeletedRoleById(UUID id) {
        return roleRepository.findByIdAndIsDeletedTrue(id)
            .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
    }

    private void softDelete(Role role) {
        role.setIsDeleted(true);
        role.setDeletedAt(Instant.now());
        role.setDeletedBy(SecurityUtil.getCurrentAccount());
    }

    private void restore(Role role) {
        role.setIsDeleted(false);
        role.setDeletedAt(null);
        role.setDeletedBy(null);
    }

}
