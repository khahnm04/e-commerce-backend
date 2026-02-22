package com.khahnm04.ecommerce.modules.auth.service.impl;

import com.khahnm04.ecommerce.modules.auth.dto.request.RoleRequest;
import com.khahnm04.ecommerce.modules.auth.dto.response.RoleResponse;
import com.khahnm04.ecommerce.modules.auth.entity.Permission;
import com.khahnm04.ecommerce.modules.auth.entity.Role;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.modules.auth.mapper.RoleMapper;
import com.khahnm04.ecommerce.modules.auth.repository.PermissionRepository;
import com.khahnm04.ecommerce.modules.auth.repository.RoleRepository;
import com.khahnm04.ecommerce.modules.auth.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleResponse createRole(RoleRequest request) {
        if (roleRepository.existsByCode(request.getName())) {
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
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    @Override
    public RoleResponse updateRole(Long id, RoleRequest request) {
        Role role = getRoleById(id);
        roleMapper.updateRole(role, request);
        assignPermissionsToRole(role, request.getPermissions());
        role = roleRepository.save(role);
        log.info("Role updated with name: {}", role.getName());
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.delete(getRoleById(id));
        log.info("Role deleted with id: {}", id);
    }

    private Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
    }

    private void assignPermissionsToRole(Role role, Set<Long> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            role.setPermissions(Collections.emptySet());
            return;
        }
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        if (permissions.size() != permissionIds.size()) {
            throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);
        }
        role.setPermissions(new HashSet<>(permissions));
    }

}
