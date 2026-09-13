package com.khahnm18.ecommercebackend.repository.auth;

import com.khahnm18.ecommercebackend.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID>, JpaSpecificationExecutor<Role> {

    Optional<Role> findByIdAndIsDeletedFalse(UUID id);
    Optional<Role> findByIdAndIsDeletedTrue(UUID id);

    @Modifying
    @Query("""
        UPDATE Role r
        SET r.isDeleted = true,
            r.deletedAt = CURRENT_TIMESTAMP,
            r.deletedBy = :accountId
        WHERE r.id IN :roleIds
        AND r.isDeleted = false
    """)
    void softDeleteAllByIds(@Param("roleIds") Set<UUID> roleIds,
                            @Param("accountId") UUID accountId);

    @Modifying
    @Query("""
        UPDATE Role r
        SET r.isDeleted = false,
            r.deletedAt = null,
            r.deletedBy = null
        WHERE r.id IN :roleIds
        AND r.isDeleted = true
    """)
    void restoreAllByIds(@Param("roleIds") Set<UUID> roleIds);

    @Modifying
    @Query("""
        DELETE FROM Role r
        WHERE r.id IN :roleIds
        AND r.isDeleted = true
    """)
    void deleteAllByIds(@Param("roleIds") Set<UUID> roleIds);

}
