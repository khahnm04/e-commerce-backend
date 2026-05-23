package com.khahnm04.ecommerce.repository.auth;

import com.khahnm04.ecommerce.entity.auth.Role;
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

    @Query("""
        SELECT rp FROM RolePermission rp
        JOIN FETCH rp.permission
        WHERE rp.role IN :roles
    """)
    List<RolePermission> findByRoleInWithPermission(@Param("roles") List<Role> roles);

    @Modifying
    @Query("""
        DELETE FROM RolePermission rp
        WHERE rp.role.id = :roleId
    """)
    void deleteByRoleId(@Param("roleId") Long roleId);

    @Modifying
    @Query("""
        DELETE FROM RolePermission rp
        WHERE rp.role.id IN :roleIds
    """)
    void deleteAllByRoleId(@Param("roleIds") Set<Long> roleIds);

    // Application Init
    Boolean existsByRoleIdAndPermissionId(Long roleId, Long permissionId);

}
