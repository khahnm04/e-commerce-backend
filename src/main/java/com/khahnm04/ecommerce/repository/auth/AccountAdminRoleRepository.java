package com.khahnm04.ecommerce.repository.auth;

import com.khahnm04.ecommerce.entity.auth.AccountAdminRole;
import com.khahnm04.ecommerce.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

@Repository
public interface AccountAdminRoleRepository extends JpaRepository<AccountAdminRole, Long> {

    List<AccountAdminRole> findByAccountAdminId(Long id);

    @Modifying
    void deleteByAccountAdminId(@Param("accountAdminId") Long accountAdminId);

    @Modifying
    @Query("DELETE FROM AccountAdminRole a WHERE a.accountAdminId IN :accountAdminIds")
    void deleteAllByAccountAdminIds(@Param("accountAdminIds") Set<Long> accountAdminIds);

    @Modifying
    @Query("DELETE FROM AccountAdminRole aar WHERE aar.id IN :ids")
    void deleteAllByIds(@Param("ids") List<Long> ids);

    @Modifying
    @Query("""
        INSERT INTO AccountAdminRole (accountAdminId, roleId)
        SELECT :accountAdminId, r.id
        FROM Role r
        WHERE r.id IN (:roleIds)
    """)
    void insertAll(@Param("accountAdminId") Long accountAdminId,
                   @Param("roleIds") List<Long> roleIds);

    @Query("""
        SELECT r FROM Role r
        JOIN AccountAdminRole ar ON r.id = ar.roleId
        WHERE ar.accountAdminId = :accountAdminId
    """)
    List<Role> findRolesByAccountAdminId(@Param("accountAdminId") Long accountAdminId);

}
