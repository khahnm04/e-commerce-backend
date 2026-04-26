package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.config.AuditorAwareConfig;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.dto.request.filter.RoleFilterRequest;
import com.khahnm04.ecommerce.dto.request.RoleRequest;
import com.khahnm04.ecommerce.dto.projection.RoleProjection;
import com.khahnm04.ecommerce.dto.response.RoleResponse;
import com.khahnm04.ecommerce.entity.Permission;
import com.khahnm04.ecommerce.entity.Role;
import com.khahnm04.ecommerce.entity.Employee;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.RoleMapper;
import com.khahnm04.ecommerce.repository.EmployeeRepository;
import com.khahnm04.ecommerce.specification.RoleSpecification;
import com.khahnm04.ecommerce.repository.PermissionRepository;
import com.khahnm04.ecommerce.repository.RoleRepository;
import com.khahnm04.ecommerce.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;
    private final AuditorAwareConfig auditorAwareConfig;

    @Override
    public RoleResponse createRole(RoleRequest request) {
        if (roleRepository.existsByCodeAndDeletedAtIsNull(request.code())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
        if (roleRepository.existsByNameAndDeletedAtIsNull(request.name())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
        Role role = roleMapper.toRole(request);
        assignPermissionsToRole(role, request.permissions());
        role = roleRepository.save(role);
        log.info("Role created with name: {}", role.getName());
        return buildRoleResponse(role);
    }

    @Override
    public RoleResponse getRoleDetail(Long id, boolean isDeleted) {
        Role role = isDeleted ? getDeletedRoleById(id) : getRoleById(id);
        return buildRoleResponse(role);
    }

    @Override
    public PageResponse<RoleResponse> getAllRoles(RoleFilterRequest filter) {
        return getAllRolesWithFilterAndSearch(filter, false);
    }

    @Override
    public PageResponse<RoleResponse> getAllDeletedRoles(RoleFilterRequest filter) {
        return getAllRolesWithFilterAndSearch(filter, true);
    }

    @Override
    @Transactional
    public RoleResponse updateRole(Long id, RoleRequest request) {
        if (roleRepository.existsByCodeAndIdNotAndDeletedAtIsNull(request.code(), id)) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
        if (roleRepository.existsByNameAndIdNotAndDeletedAtIsNull(request.name(), id)) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
        Role role = getRoleById(id);
        roleMapper.updateRole(role, request);
        assignPermissionsToRole(role, request.permissions());
        role = roleRepository.save(role);
        log.info("Role updated with name: {}", role.getName());
        return buildRoleResponse(role);
    }

    @Override
    @Transactional
    public void softDeleteRole(Long id) {
        Role role = getRoleById(id);
        if (role.getDeletedAt() != null) {
            log.warn("Role with id {} is already soft-deleted", id);
            return;
        }
        role.setDeletedAt(LocalDateTime.now());
        role.setDeletedBy(auditorAwareConfig.getCurrentAuditor().orElse(null));
        roleRepository.save(role);
        log.info("Soft-deleted role with id: {}", id);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        Role role = getDeletedRoleById(id);
        if (role.getDeletedAt() == null) {
            log.warn("Role with id {} is already deleted", id);
            return;
        }
        if (employeeRepository.existsByRoles_Id(role.getId())) {
            throw new AppException(ErrorCode.ROLE_HAS_USERS);
        }
        roleRepository.delete(role);
        log.info("Deleted role with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreRole(Long id) {
        Role role = getDeletedRoleById(id);
        if (role.getDeletedAt() == null) {
            log.warn("Role with id {} is not in a deleted state", id);
            return;
        }
        role.setDeletedAt(null);
        role.setDeletedBy(null);
        roleRepository.save(role);
        log.info("Role restored with id: {}", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(BulkActionRequest request) {
        Set<Long> roleIds = request.ids();
        String action = request.action().toUpperCase();

        if (roleIds == null || roleIds.isEmpty()) {
            throw new AppException(ErrorCode.ROLE_INVALID_BULK_ACTION);
        }

        switch (action) {
            case "SOFT_DELETE" -> {
                Employee currentAuditor = auditorAwareConfig.getCurrentAuditorReference();
                roleRepository.softDeleteByIds(LocalDateTime.now(), currentAuditor, roleIds);
                log.info("Bulk soft-delete roles ids: {}", roleIds);
            }
            case "RESTORE" -> {
                roleRepository.restoreByIds(roleIds);
                log.info("Bulk restored roles with IDs: {}", roleIds);
            }
            case "DELETE" -> {
                if (employeeRepository.existsByRoles_IdIn(roleIds)) {
                    throw new AppException(ErrorCode.ROLE_HAS_USERS);
                }
                List<Role> roles = roleRepository.findAllDeletedByIds(roleIds);
                roleRepository.deleteAll(roles);
                log.info("Bulk permanently deleted roles with IDs: {}", roleIds);
            }
            default -> throw new AppException(ErrorCode.ROLE_INVALID_BULK_ACTION);
        }
    }

    // ======================== Private methods ========================

    private PageResponse<RoleResponse> getAllRolesWithFilterAndSearch(RoleFilterRequest filter, boolean isDeleted) {
        Pageable pageable = PageRequest.of(filter.page() - 1, filter.size(),
                SortUtils.parseSort(filter.sort()));

        Specification<Role> spec = RoleSpecification.buildSpecification(filter, isDeleted);

        Page<RoleProjection> rolePage = roleRepository
                .findBy(spec, q -> q.as(RoleProjection.class).page(pageable));

        List<RoleResponse> content = rolePage.getContent().stream()
                .map(proj -> RoleResponse.builder()
                        .id(proj.getId())
                        .code(proj.getCode())
                        .name(proj.getName())
                        .description(proj.getDescription())
                        .createdAt(proj.getCreatedAt())
                        .createdBy(proj.getCreatedByFullName())
                        .updatedAt(proj.getUpdatedAt())
                        .updatedBy(proj.getUpdatedByFullName())
                        .deletedAt(proj.getDeletedAt())
                        .deletedBy(proj.getDeletedByFullName())
                        .build())
                .collect(Collectors.toList());

        Page<RoleResponse> page = new PageImpl<>(content, pageable, rolePage.getTotalElements());
        return PageResponse.fromPage(page);
    }

    private Role getRoleById(Long id) {
        return roleRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
    }

    private Role getDeletedRoleById(Long id) {
        return roleRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
    }

    private void assignPermissionsToRole(Role role, Set<Long> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            role.setPermissions(Collections.emptySet());
            return;
        }
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        if (permissions.size() != permissionIds.size()) {
            List<Long> foundIds = permissions.stream()
                    .map(Permission::getId)
                    .toList();

            List<Long> missingIds = permissionIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            log.error("Failed to assign permissions to role: {}. Missing Permission IDs: {}", role.getName(),
                    missingIds);
            throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);
        }
        role.setPermissions(new HashSet<>(permissions));
    }

    private RoleResponse buildRoleResponse(Role role) {
        RoleResponse response = roleMapper.toRoleResponse(role);
        Set<Permission> permissions = role.getPermissions();
        Set<String> PermissionCodes = permissions.stream()
                .map(Permission::getCode)
                .collect(Collectors.toSet());
        return response.toBuilder().permissions(PermissionCodes).build();
    }

}
