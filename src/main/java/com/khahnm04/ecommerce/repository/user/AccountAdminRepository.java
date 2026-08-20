package com.khahnm04.ecommerce.repository.user;

import com.khahnm04.ecommerce.constant.user.AccountAdminStatus;
import com.khahnm04.ecommerce.entity.user.AccountAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import java.util.Set;

@Repository
public interface AccountAdminRepository extends JpaRepository<AccountAdmin, Long>, JpaSpecificationExecutor<AccountAdmin> {

    boolean existsByEmailAndIsDeletedFalse(String email);
    boolean existsByEmailAndIdNotAndIsDeletedFalse(String email, Long id);

    boolean existsByPhoneNumberAndIsDeletedFalse(String phoneNumber);
    boolean existsByPhoneNumberAndIdNotAndIsDeletedFalse(String phoneNumber, Long id);

    Optional<AccountAdmin> findByIdAndIsDeletedFalse(Long id);
    Optional<AccountAdmin> findByIdAndIsDeletedTrue(Long id);
    Optional<AccountAdmin> findByEmailAndIsDeletedFalse(String email);

    @Modifying
    @Query("""
        UPDATE AccountAdmin a
        SET a.status = :status
        WHERE a.id IN :ids
        AND a.isDeleted = false
    """)
    void updateStatusAllByIds(@Param("status") AccountAdminStatus status,
                           @Param("ids") Set<Long> ids);

    @Modifying
    @Query("""
        UPDATE AccountAdmin a
        SET a.isDeleted = true,
            a.deletedAt = CURRENT_TIMESTAMP,
            a.deletedBy = :user
        WHERE a.id IN :accountAdminIds
        AND a.isDeleted = false
    """)
    void softDeleteAllByIds(@Param("accountAdminIds") Set<Long> accountAdminIds,
                            @Param("user") String user);

    @Modifying
    @Query("""
        UPDATE AccountAdmin a
        SET a.isDeleted = false,
            a.deletedAt = null,
            a.deletedBy = null
        WHERE a.id IN :accountAdminIds
        AND a.isDeleted = true
    """)
    void restoreAllByIds(@Param("accountAdminIds") Set<Long> accountAdminIds);

    @Modifying
    @Query("""
        DELETE FROM AccountAdmin a
        WHERE a.id IN :accountAdminIds
        AND a.isDeleted = true
    """)
    void deleteAllByIds(Set<Long> accountAdminIds);

    @Query("""
        SELECT CASE WHEN EXISTS (
            SELECT 1
            FROM AccountAdmin a
            JOIN AccountAdminRole ar ON ar.accountAdminId = a.id
            JOIN Role r ON ar.roleId = r.id
            WHERE a.id = :userId
            AND r.code = 'ADMIN'
            AND a.isDeleted = false
        ) THEN true ELSE false END
    """)
    boolean isAdmin(@Param("userId") Long userid);

    @Query("""
        SELECT DISTINCT p.code
        FROM AccountAdmin a
        JOIN AccountAdminRole ar ON a.id = ar.accountAdminId
        JOIN RolePermission rp ON ar.roleId = rp.roleId
        JOIN Permission p ON rp.permissionId = p.id
        WHERE a.id = :userId
        AND a.isDeleted = false
    """)
    List<String> findPermissionCodesById(@Param("userId") Long userid);

}
