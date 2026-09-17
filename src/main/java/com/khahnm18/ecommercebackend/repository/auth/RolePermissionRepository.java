package com.khahnm18.ecommercebackend.repository.auth;

import com.khahnm18.ecommercebackend.entity.auth.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, UUID> {

    @Modifying
    void deleteByRoleIdAndPermissionIdIn(UUID roleId, List<UUID> permissionIds);

}
