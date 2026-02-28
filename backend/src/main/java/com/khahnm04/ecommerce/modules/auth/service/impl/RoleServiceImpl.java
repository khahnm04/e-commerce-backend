package com.khahnm04.ecommerce.modules.auth.service.impl;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.auth.dto.request.RoleBulkActionRequest;
import com.khahnm04.ecommerce.modules.auth.dto.request.RoleFilterRequest;
import com.khahnm04.ecommerce.modules.auth.dto.request.RoleRequest;
import com.khahnm04.ecommerce.modules.auth.dto.response.RoleResponse;
import com.khahnm04.ecommerce.modules.auth.entity.Permission;
import com.khahnm04.ecommerce.modules.auth.entity.Role;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.modules.auth.enums.RoleSortField;
import com.khahnm04.ecommerce.modules.auth.mapper.RoleMapper;
import com.khahnm04.ecommerce.modules.auth.mapper.mybaits.RoleMyBatisMapper;
import com.khahnm04.ecommerce.modules.auth.repository.PermissionRepository;
import com.khahnm04.ecommerce.modules.auth.repository.RoleRepository;
import com.khahnm04.ecommerce.modules.auth.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;
    private final RoleMyBatisMapper roleMyBatisMapper;

    @Override
    public RoleResponse createRole(RoleRequest request) {
        if (roleRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
        if (roleRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
        Role role = roleMapper.toRole(request);
        assignPermissionsToRole(role, request.getPermissions());
        role = roleRepository.save(role);
        log.info("Role created with name: {}", role.getName());
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public RoleResponse getRoleDetailById(Long id) {
        return roleMapper.toRoleResponse(getRoleById(id));
    }

    @Override
    public RoleResponse getDeletedRoleDetailById(Long id) {;
        return roleMapper.toRoleResponse(getDeletedRoleById(id));
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
    public RoleResponse updateRole(Long id, RoleRequest request) {
        if (roleRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
        if (roleRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
        Role role = getRoleById(id);
        roleMapper.updateRole(role, request);
        assignPermissionsToRole(role, request.getPermissions());
        role = roleRepository.save(role);
        log.info("Role updated with name: {}", role.getName());
        return roleMapper.toRoleResponse(role);
    }

    @Override
    @Transactional
    public void softDeleteRole(Long id) {
        Role role = getRoleById(id);
        role.setDeletedAt(LocalDateTime.now());
        roleRepository.save(role);
        log.info("Role soft deleted with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreRole(Long id) {
        Role role = getDeletedRoleById(id);
        role.setDeletedAt(null);
        roleRepository.save(role);
        log.info("Role restored with id: {}", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(RoleBulkActionRequest request) {
        Set<Long> roleIds = request.getRoles();
        String action = request.getAction().toUpperCase();

        switch (action) {
            case "DELETE" -> {
                List<Role> roles = roleRepository.findAllById(roleIds);
                if (roles.size() != roleIds.size()) {
                    throw new AppException(ErrorCode.ROLE_NOT_FOUND);
                }
                roleRepository.softDeleteByIds(LocalDateTime.now(), roleIds);
                log.info("Bulk soft deleted roles ids: {}", roleIds);
            }
            case "RESTORE" -> {
                List<Role> deletedRoles = roleRepository.findAllDeletedByIds(roleIds);
                if (deletedRoles.size() != roleIds.size()) {
                    throw new AppException(ErrorCode.ROLE_NOT_FOUND);
                }
                roleRepository.restoreByIds(roleIds);
                log.info("Bulk restored roles ids: {}", roleIds);
            }
            default -> throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
    }

    // ======================== Private methods ========================

    private PageResponse<RoleResponse> getAllRolesWithFilterAndSearch(RoleFilterRequest filter, boolean isDeleted) {
        Pageable pageable = PageRequest.of(filter.getPage() - 1, filter.getSize(),
                SortUtils.parseSort(filter.getSort()));

        String keyword = StringUtils.hasText(filter.getKeyword())
                ? filter.getKeyword().trim()
                : null;

        // Parse sort for MyBatis
        String[] sortParams = SortUtils.parseSortForMyBatis(filter.getSort(), RoleSortField.class);
        String sortColumn = sortParams[0];
        String sortDirection = sortParams[1];

        List<RoleResponse> content = roleMyBatisMapper.findAllWithFilterAndSearch(
                keyword,
                isDeleted,
                sortColumn,
                sortDirection,
                pageable.getOffset(),
                pageable.getPageSize());

        long total = roleMyBatisMapper.countWithFilterAndSearch(keyword, isDeleted);

        Page<RoleResponse> page = new PageImpl<>(content, pageable, total);
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

}
