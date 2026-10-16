package com.khahnm18.ecommercebackend.repository.auth;

import com.khahnm18.ecommercebackend.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByIdAndIsDeletedFalse(UUID id);
    Optional<Role> findByIdAndIsDeletedTrue(UUID id);

    Optional<Role> findByCodeAndIsDeletedFalse(String code);

    @Modifying
    void deleteAllByIdInAndIsDeletedTrue(Set<UUID> roleIds);

    @Query("""
        SELECT r FROM Role r
        JOIN AccountRole ar ON ar.roleId = r.id
        WHERE ar.accountId = :accountId
    """)
    List<Role> findRolesByAccountId(@Param("accountId") UUID accountId);

}
