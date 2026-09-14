package com.khahnm18.ecommercebackend.repository.auth;

import com.khahnm18.ecommercebackend.constant.auth.AccountStatus;
import com.khahnm18.ecommercebackend.entity.auth.Account;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID>, JpaSpecificationExecutor<Account> {

    Optional<Account> findByIdAndIsDeletedFalse(UUID accountId);
    Optional<Account> findByIdAndIsDeletedTrue(UUID accountId);

    Optional<Account> findByEmailAndIsDeletedFalse(String email);

    @Modifying
    @Query("""
        UPDATE Account a
        SET a.status = :status
        WHERE a.id IN :accountIds
        AND a.isDeleted = false
    """)
    void updateStatusAllByIds(@Param("status") AccountStatus status,
                              @Param("accountIds") Set<UUID> accountIds);

    @Modifying
    @Query("""
        UPDATE Account a
        SET a.isDeleted = true,
            a.deletedAt = CURRENT_TIMESTAMP,
            a.deletedBy = :accountId
        WHERE a.id IN :accountIds
        AND r.isDeleted = false
    """)
    void softDeleteAllByIds(@Param("accountIds") Set<UUID> accountIds,
                            @Param("accountId") UUID accountId);

    @Modifying
    @Query("""
        UPDATE Account a
        SET a.isDeleted = false,
            a.deletedAt = null,
            a.deletedBy = null
        WHERE a.id IN :accountIds
        AND r.isDeleted = true
    """)
    void restoreAllByIds(@Param("accountIds") Set<UUID> accountIds);

    @Modifying
    @Query("""
        DELETE FROM Account a
        WHERE a.id IN :accountIds
        AND a.isDeleted = true
    """)
    void deleteAllByIds(@Param("accountIds") Set<UUID> accountIds);

    @Modifying
    @Transactional
    @Query("""
        UPDATE Account a
        SET a.lastLoginAt = :now, a.updatedBy = :accountId
        WHERE a.id = :accountId
    """)
    void updateLastLoginAt(@Param("accountId") UUID accountId,
                           @Param("now") Instant now);

    @Query("""
        SELECT CASE WHEN EXISTS (
            SELECT 1
            FROM Account a
            JOIN AccountRole ar ON ar.accountId = a.id
            JOIN Role r ON ar.roleId = r.id
            WHERE a.id = :accountId
            AND r.code = 'ADMIN'
            AND a.isDeleted = false
        ) THEN true ELSE false END
    """)
    boolean isAdmin(@Param("accountId") UUID accountId);

    @Query("""
        SELECT DISTINCT p.code
        FROM Account a
        JOIN AccountRole ar ON a.id = ar.accountId
        JOIN RolePermission rp ON ar.roleId = rp.roleId
        JOIN Permission p ON rp.permissionId = p.id
        WHERE a.id = :accountId
        AND a.isDeleted = false
    """)
    List<String> findAllPermissionCodesByAccountId(@Param("accountId") UUID accountId);

}
