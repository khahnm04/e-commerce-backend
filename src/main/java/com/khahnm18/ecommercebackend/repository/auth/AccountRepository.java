package com.khahnm18.ecommercebackend.repository.auth;

import com.khahnm18.ecommercebackend.entity.auth.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
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
    void deleteAllByIdInAndIsDeletedTrue(Set<UUID> accountIds);

    @Query("""
        SELECT DISTINCT p.code
        FROM AccountRole ar
        JOIN RolePermission rp ON rp.roleId =  ar.roleId
        JOIN Permission p ON p.id = rp.permissionId
        WHERE ar.id = :accountId
    """)
    List<String> findAllPermissionCodesByAccountId(@Param("accountId") UUID accountId);

}
