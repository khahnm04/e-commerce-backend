package com.khahnm18.ecommercebackend.repository.auth;

import com.khahnm18.ecommercebackend.entity.auth.Permission;
import java.util.List;
import java.util.UUID;

public interface CustomPermissionRepository {

    List<Permission> findPermissionsByRoleId(UUID roleId);

    List<Permission> findAllPermissionByAccountId(UUID accountId);

}
