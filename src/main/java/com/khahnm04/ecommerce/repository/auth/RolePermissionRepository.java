package com.khahnm04.ecommerce.repository.auth;

import com.khahnm04.ecommerce.entity.auth.Permission;
import com.khahnm04.ecommerce.entity.auth.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    List<RolePermission> findByRoleId(Long id);

    @Modifying
    void deleteByRoleId(@Param("roleId") Long roleId);

    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.roleId IN :roleIds")
    void deleteAllByRoleIds(@Param("roleIds") Set<Long> roleIds);

    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.id IN :ids")
    void deleteAllByIds(@Param("ids") List<Long> ids);

    @Modifying
    @Query("""
        INSERT INTO RolePermission (roleId, permissionId)
        SELECT :roleId, p.id
        FROM Permission p
        WHERE p.id IN (:permissionIds)
    """)
    void insertAll(@Param("roleId") Long roleId,
                   @Param("permissionIds") List<Long> permissionIds);

    @Query("""
        SELECT p FROM Permission p
        JOIN RolePermission rp ON rp.permissionId = p.id
        WHERE rp.roleId = :roleId
    """)
    List<Permission> findPermissionsByRoleId(@Param("roleId") Long roleId);

}
