package com.khahnm18.ecommercebackend.repository.auth;

import com.khahnm18.ecommercebackend.entity.auth.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {

    @Query("""
        SELECT p FROM Permission p
        JOIN RolePermission rp ON rp.permissionId = p.id
        WHERE rp.roleId = :roleId
    """)
    List<Permission> findPermissionsByRoleId(@Param("roleId") UUID roleId);

    @Query("""
        SELECT p FROM AccountRole ar
        JOIN RolePermission rp ON rp.roleId = ar.roleId
        JOIN Permission p ON p.id = rp.permissionId
        WHERE ar.accountId = :accountId
    """)
    List<Permission> findAllPermissionByAccountId(@Param("accountId") UUID accountId);

}
