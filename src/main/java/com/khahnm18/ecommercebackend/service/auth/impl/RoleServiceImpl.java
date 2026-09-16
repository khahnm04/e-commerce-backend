package com.khahnm18.ecommercebackend.service.auth.impl;

import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.auth.RoleRequest;
import com.khahnm18.ecommercebackend.dto.response.auth.RoleResponse;
import com.khahnm18.ecommercebackend.entity.auth.Role;
import com.khahnm18.ecommercebackend.entity.auth.Permission;
import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.mapper.auth.RoleMapper;
import com.khahnm18.ecommercebackend.service.auth.RolePermissionService;
import com.khahnm18.ecommercebackend.service.auth.RoleService;
import com.khahnm18.ecommercebackend.repository.auth.RoleRepository;
import com.khahnm18.ecommercebackend.repository.auth.RolePermissionRepository;
import com.khahnm18.ecommercebackend.service.auth.cache.PermissionCacheService;
import com.khahnm18.ecommercebackend.specification.RoleSpecification;
import com.khahnm18.ecommercebackend.util.PageableUtil;
import com.khahnm18.ecommercebackend.util.SecurityUtil;
import org.springframework.data.jpa.domain.Specification;
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

    private final RolePermissionService rolePermissionService;
    private final PermissionCacheService permissionCacheService;
    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest request) {
        Role role = roleMapper.toRole(request);
        role = this.saveRole(role);
        List<Permission> permissions = rolePermissionService.assignPermissionsToRole(role, request.permissions());
        log.info("Role created success: code={}, name={}", role.getCode(), role.getName());
        return roleMapper.toRoleResponseWithPermissions(role, permissions);
    }

    @Override
    public RoleResponse getRoleDetailById(UUID id, boolean isDeleted) {
        Role role = isDeleted ? this.getSoftDeletedRoleById(id) : this.getRoleById(id);
        List<Permission> permissions = rolePermissionRepository.findPermissionsByRoleId(role.getId());
        return roleMapper.toRoleResponseWithPermissions(role, permissions);
    }

    @Override
    public Page<RoleResponse> getAllRoles(
        int page,
        int size,
        String sort,
        String keyword,
        boolean isDeleted,
        Instant startDate,
        Instant endDate
    ) {
        Pageable pageable = PageableUtil.createPageable(page, size, sort);

        Specification<Role> spec = RoleSpecification.byIsDeleted(isDeleted)
            .and(RoleSpecification.byKeyword(keyword))
            .and(RoleSpecification.byStartDate(startDate))
            .and(RoleSpecification.byEndDate(endDate));

        Page<Role> rolePage = roleRepository.findAll(spec, pageable);
        return rolePage.map(roleMapper::toRoleResponse);
    }

    @Override
    @Transactional
    public RoleResponse updateRole(UUID id, RoleRequest request) {
        Role role = getRoleById(id);
        roleMapper.updateRole(role, request);
        role = this.saveRole(role);
        List<Permission> permissions = rolePermissionService.replacePermissionsForRole(role, request.permissions());
        log.info("Role updated success: code={}, name={}", role.getCode(), role.getName());
        return roleMapper.toRoleResponseWithPermissions(role, permissions);
    }

    @Override
    @Transactional
    public void softDeleteRole(UUID id) {
        Role role = getRoleById(id);
        role.setIsDeleted(true);
        role.setDeletedAt(Instant.now());
        role.setDeletedBy(SecurityUtil.getCurrentAccount());
        permissionCacheService.evictAll();
        log.info("Role soft deleted success with id: {}", id);
    }

    @Override
    @Transactional
    public void deleteRole(UUID id) {
        Role role = getSoftDeletedRoleById(id);
        rolePermissionRepository.deleteByRoleId(id);
        roleRepository.delete(role);
        permissionCacheService.evictAll();
        log.info("Role deleted success with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreRole(UUID id) {
        Role role = getSoftDeletedRoleById(id);
        role.setIsDeleted(false);
        role.setDeletedAt(null);
        role.setDeletedBy(null);
        permissionCacheService.evictAll();
        log.info("Role restored success with id: {}", id);
    }

    @Override
    @Transactional
    public void bulkAction(BulkActionRequest request) {
        Set<UUID> roleIds = request.ids();
        String action = request.action().toUpperCase();
        switch (action) {
            case "SOFT_DELETE" -> {
                roleRepository.softDeleteAllByIds(roleIds, SecurityUtil.getCurrentAccount());
                permissionCacheService.evictAll();
                log.info("Bulk action soft delete roles ids: {}", roleIds);
            }
            case "RESTORE" -> {
                roleRepository.restoreAllByIds(roleIds);
                permissionCacheService.evictAll();
                log.info("Bulk action restore roles ids: {}", roleIds);
            }
            case "DELETE" -> {
                rolePermissionRepository.deleteAllByRoleIdIn(roleIds);
                roleRepository.deleteAllByIds(roleIds);
                permissionCacheService.evictAll();
                log.info("Bulk action delete roles ids: {}", roleIds);
            }
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

    private Role saveRole(Role role) {
        try {
            return roleRepository.save(role);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Role save failed - duplicate entry: name={}", role.getName());
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
    }

}
