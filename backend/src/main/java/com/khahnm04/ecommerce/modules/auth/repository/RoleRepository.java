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

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByName(String name);

    boolean existsByCode(String code);

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsByCodeAndIdNot(String code, Long id);

    @Query(value = "SELECT * FROM roles WHERE id = :id", nativeQuery = true)
    Optional<Role> findDeletedById(@Param("id") Long id);

    @Query(value = "SELECT * FROM roles WHERE id IN :ids", nativeQuery = true)
    List<Role> findAllDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Query(value = "UPDATE roles SET deleted_at = NULL WHERE id = :id", nativeQuery = true)
    void restoreById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Role r SET r.deletedAt = :now WHERE r.id IN :ids")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("ids") Set<Long> ids);

    @Modifying
    @Query(value = "UPDATE roles SET deleted_at = NULL WHERE id IN :ids", nativeQuery = true)
    void restoreByIds(@Param("ids") Set<Long> ids);

}
