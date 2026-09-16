package com.khahnm18.ecommercebackend.repository.auth;

import com.khahnm18.ecommercebackend.entity.auth.Permission;
import com.khahnm18.ecommercebackend.entity.auth.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, UUID> {

    @Query("""
        SELECT p FROM Permission p
        JOIN RolePermission rp
        ON rp.permissionId = p.id
        WHERE rp.roleId = :roleId
    """)
    List<Permission> findPermissionsByRoleId(@Param("roleId") UUID roleId);

    @Modifying
    void deleteByRoleId(UUID roleId);

    @Modifying
    void deleteAllByRoleIdIn(Set<UUID> roleIds);

    @Modifying
    void deleteByRoleIdAndPermissionIdIn(UUID roleId, List<UUID> permissionIds);

}
