package com.khahnm18.ecommercebackend.repository.auth;

import com.khahnm18.ecommercebackend.entity.auth.AccountRole;
import com.khahnm18.ecommercebackend.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole, UUID> {

    List<AccountRole> findByAccountId(UUID accountId);

    @Query("""
        SELECT r FROM Role r
        JOIN AccountRole ar ON r.id = ar.roleId
        WHERE ar.accountId = :accountId
    """)
    List<Role> findRolesByAccountId(@Param("accountId") UUID accountId);

    @Modifying
    void deleteByAccountId(UUID accountId);

    @Modifying
    void deleteAllByAccountIdIn(Set<UUID> accountIds);

    @Modifying
    void deleteAllByIdIn(List<UUID> ids);

    @Modifying
    @Query("""
        INSERT INTO AccountRole (accountId, roleId)
        SELECT :accountId, r.id
        FROM Role r
        WHERE r.id IN (:roleIds)
    """)
    void insertAll(@Param("accountId") UUID accountId,
                   @Param("roleIds") List<UUID> roleIds);

}
