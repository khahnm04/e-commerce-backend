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

    List<RolePermission> findByRoleId(UUID id);

    @Query("""
        SELECT p FROM Permission p
        JOIN RolePermission rp
        ON rp.permissionId = p.id
        WHERE rp.roleId = :roleId
    """)
    List<Permission> findPermissionsByRoleId(@Param("roleId") UUID roleId);

    @Modifying
    void deleteByRoleId(@Param("roleId") UUID roleId);

    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.roleId IN :roleIds")
    void deleteAllByRoleIds(@Param("roleIds") Set<UUID> roleIds);

    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.id IN :ids")
    void deleteAllByIds(@Param("ids") List<UUID> ids);

    @Modifying
    @Query("""
        INSERT INTO RolePermission (roleId, permissionId)
        SELECT :roleId, p.id
        FROM Permission p
        WHERE p.id IN (:permissionIds)
    """)
    void insertAll(@Param("roleId") UUID roleId,
                   @Param("permissionIds") List<UUID> permissionIds);

}
