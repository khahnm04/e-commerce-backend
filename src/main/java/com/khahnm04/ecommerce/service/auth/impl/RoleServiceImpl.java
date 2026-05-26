package com.khahnm04.ecommerce.service.auth.impl;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.response.auth.PermissionResponse;
import com.khahnm04.ecommerce.util.PaginationUtil;
import com.khahnm04.ecommerce.util.SecurityUtil;
import com.khahnm04.ecommerce.util.SortUtil;
import com.khahnm04.ecommerce.dto.request.filter.RoleFilterRequest;
import com.khahnm04.ecommerce.dto.projection.RoleProjection;
import com.khahnm04.ecommerce.dto.request.auth.RoleRequest;
import com.khahnm04.ecommerce.dto.response.auth.RoleResponse;
import com.khahnm04.ecommerce.entity.auth.Permission;
import com.khahnm04.ecommerce.entity.auth.Role;
import com.khahnm04.ecommerce.entity.auth.RolePermission;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.auth.PermissionMapper;
import com.khahnm04.ecommerce.mapper.auth.RoleMapper;
import com.khahnm04.ecommerce.repository.auth.PermissionRepository;
import com.khahnm04.ecommerce.repository.auth.RolePermissionRepository;
import com.khahnm04.ecommerce.repository.auth.RoleRepository;
import com.khahnm04.ecommerce.service.auth.RoleService;
import com.khahnm04.ecommerce.specification.auth.RoleSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j(topic = "ROLE-SERVICE")
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest request) {
        if (roleRepository.existsByCodeAndIsDeletedFalse(request.code())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }

        Role role = roleMapper.toRole(request);
        role = roleRepository.save(role);
        List<Permission> permissions = assignPermissionsToRole(role, request.permissions());

        RoleResponse response = roleMapper.toRoleResponse(role)
            .toBuilder()
            .permissions(permissions.stream()
                .map(permissionMapper::toPermissionResponse)
                .collect(Collectors.toSet()))
            .build();

        log.info("Role created: code={}, name={}", role.getCode(), role.getName());
        return response;
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
    }

    @Override
    public Role getSoftDeletedRoleById(Long id) {
        return roleRepository.findByIdAndIsDeletedTrue(id)
            .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
    }

    @Override
    public RoleResponse getRoleDetailById(Long id, boolean isDeleted) {
        Role role = isDeleted ? getSoftDeletedRoleById(id) : getRoleById(id);
        List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(role.getId());

        Set<PermissionResponse> permissions = rolePermissions.stream()
            .map(rp -> permissionMapper.toPermissionResponse(rp.getPermission()))
            .collect(Collectors.toSet());

        return roleMapper.toRoleResponse(role)
            .toBuilder()
            .permissions(permissions)
            .build();
    }

    @Override
    public Page<RoleResponse> getAllRoles(RoleFilterRequest filter) {
        return getAllRolesWithFilterAndSearch(filter, filter.isDeleted());
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
        roleRepository.save(role);

        RoleResponse response = roleMapper.toRoleResponse(role)
            .toBuilder()
            .permissions(permissions.stream()
                .map(permissionMapper::toPermissionResponse)
                .collect(Collectors.toSet()))
            .build();

        log.info("Role updated: code={}, name={}", role.getCode(), role.getName());
        return response;
    }

    @Override
    @Transactional
    public void softDeleteRole(Long id) {
        Role role = getRoleById(id);
        role.setIsDeleted(true);
        role.setDeletedAt(Instant.now());
        role.setDeletedBy(SecurityUtil.getCurrentUser());
        roleRepository.save(role);
        log.info("Soft-deleted role with id: {}", id);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        Role role = getSoftDeletedRoleById(id);
        rolePermissionRepository.deleteByRoleId(id);
        roleRepository.delete(role);
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
        log.info("Role restored with id: {}", id);
    }

    @Override
    @Transactional
    public void bulkAction(BulkActionRequest request) {
        Set<Long> roleIds = request.ids();
        String action = request.action().toUpperCase();
        switch (action) {
            case "SOFT_DELETE" -> {
                roleRepository.softDeleteByIds(roleIds, SecurityUtil.getCurrentUser());
                log.info("Bulk action soft delete roles ids: {}", roleIds);
            }
            case "RESTORE" -> {
                roleRepository.restoreByIds(roleIds);
                log.info("Bulk action restore roles ids: {}", roleIds);
            }
            case "DELETE" -> {
                rolePermissionRepository.deleteAllByRoleId(roleIds);
                roleRepository.deleteAllByIds(roleIds);
                log.info("Bulk action delete roles ids: {}", roleIds);
            }
        }
    }

    private List<Permission> assignPermissionsToRole(Role role, Set<Long> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            throw new AppException(ErrorCode.PERMISSION_REQUIRED);
        }

        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        if (permissions.size() != permissionIds.size()) {
            throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);
        }

        List<RolePermission> rolePermissions = permissions.stream()
            .map(permission -> RolePermission.builder()
                .role(role)
                .permission(permission)
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

        List<RolePermission> existing = rolePermissionRepository.findByRoleId(role.getId());

        List<RolePermission> toDelete = existing.stream()
            .filter(rolePermission -> !permissionIds.contains(rolePermission.getPermission().getId()))
            .toList();

        Set<Long> existingPermissionIds = existing.stream()
            .map(rolePermission -> rolePermission.getPermission().getId())
            .collect(Collectors.toSet());

        List<RolePermission> toAdd = permissions.stream()
            .filter(permission -> !existingPermissionIds.contains(permission.getId()))
            .map(permission -> RolePermission.builder()
                .role(role)
                .permission(permission)
                .build())
            .collect(Collectors.toList());

        rolePermissionRepository.deleteAll(toDelete);
        rolePermissionRepository.saveAll(toAdd);
        return permissions;
    }

    private Page<RoleResponse> getAllRolesWithFilterAndSearch(RoleFilterRequest request, Boolean isDeleted) {
        Pageable pageable = PaginationUtil.createPageable(
            request.page(),
            request.size(),
            request.sort()
        );

        Page<RoleProjection> rolePage = roleRepository.findBy(
            RoleSpecification.buildQuery(request, isDeleted),
            q -> q.as(RoleProjection.class).page(pageable)
        );

        List<RoleResponse> content = rolePage.getContent().stream()
            .map(roleProjection -> RoleResponse.builder()
                .id(roleProjection.getId())
                .code(roleProjection.getCode())
                .name(roleProjection.getName())
                .description(roleProjection.getDescription())
                .createdAt(roleProjection.getCreatedAt())
                .createdBy(roleProjection.getCreatedBy())
                .updatedAt(roleProjection.getUpdatedAt())
                .updatedBy(roleProjection.getUpdatedBy())
                .deletedAt(roleProjection.getDeletedAt())
                .deletedBy(roleProjection.getDeletedBy())
                .build())
            .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, rolePage.getTotalElements());
    }

}
