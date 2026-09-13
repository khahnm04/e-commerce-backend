package com.khahnm18.ecommercebackend.service.auth;

import com.khahnm18.ecommercebackend.entity.auth.Permission;
import com.khahnm18.ecommercebackend.entity.auth.Role;
import java.util.List;
import java.util.UUID;

public interface RolePermissionService {

    List<Permission> assignPermissionsToRole(Role role, List<UUID> permissionIds);

    List<Permission> replacePermissionsForRole(Role role, List<UUID> permissionIds);

}
