package com.khahnm04.ecommerce.repository.auth;

import com.khahnm04.ecommerce.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    boolean existsByCodeAndIsDeletedFalse(String code);

    boolean existsByCodeAndIdNotAndIsDeletedFalse(String code, Long id);

    Optional<Role> findByIdAndIsDeletedFalse(Long id);

    Optional<Role> findByIdAndIsDeletedTrue(Long id);

    @Modifying
    @Query("""
        UPDATE Role r
        SET r.isDeleted = true,
            r.deletedAt = CURRENT_TIMESTAMP,
            r.deletedBy = :user
        WHERE r.id IN :roleIds
        AND r.isDeleted = false
    """)
    void softDeleteByIds(@Param("roleIds") Set<Long> roleIds,
                         @Param("user") String user);

    @Modifying
    @Query("""
        UPDATE Role r
        SET r.isDeleted = false,
            r.deletedAt = null,
            r.deletedBy = null
        WHERE r.id IN :roleIds
        AND r.isDeleted = true
    """)
    void restoreByIds(@Param("roleIds") Set<Long> roleIds);

    @Modifying
    @Query("""
        DELETE FROM Role r
        WHERE r.id IN :roleIds
        AND r.isDeleted = true
    """)
    void deleteAllByIds(Set<Long> roleIds);

    // Application Init
    Optional<Role> findByCodeAndIsDeletedFalse(String code);

}
