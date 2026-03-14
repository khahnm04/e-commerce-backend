package com.khahnm04.ecommerce.modules.auth.repository;

import com.khahnm04.ecommerce.modules.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    boolean existsByCodeAndDeletedAtIsNull(String code);

    boolean existsByNameAndDeletedAtIsNull(String name);

    boolean existsByNameAndIdNotAndDeletedAtIsNull(String name, Long id);

    boolean existsByCodeAndIdNotAndDeletedAtIsNull(String code, Long id);

    Optional<Role> findByIdAndDeletedAtIsNull(Long id);

    Optional<Role> findByIdAndDeletedAtIsNotNull(Long id);

    @Query("SELECT r FROM Role r WHERE r.id IN :ids AND r.deletedAt IS NOT NULL")
    List<Role> findAllDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Query("UPDATE Role r SET r.deletedAt = :now WHERE r.id IN :ids")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("ids") Set<Long> ids);

    @Modifying
    @Query("UPDATE Role r SET r.deletedAt = NULL WHERE r.id IN :ids")
    void restoreByIds(@Param("ids") Set<Long> ids);

}
