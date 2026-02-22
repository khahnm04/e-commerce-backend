package com.khahnm04.ecommerce.modules.branch.repository;

import com.khahnm04.ecommerce.modules.branch.entity.Branch;
import com.khahnm04.ecommerce.modules.branch.enums.BranchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    Optional<Branch> findByCode(String code);

    boolean existsByCode(String code);

    @Query(value = "SELECT * FROM branches WHERE id = :id", nativeQuery = true)
    Optional<Branch> findDeletedBranchById(@Param("id") Long id);

    boolean existsByName(String name);

    @Query("SELECT COUNT(b) > 0 FROM Branch b WHERE b.code = :code AND b.id != :id")
    boolean existsByCodeAndIdNot(@Param("code") String code, @Param("id") Long id);

    @Query("SELECT COUNT(b) > 0 FROM Branch b WHERE b.name = :name AND b.id != :id")
    boolean existsByNameAndIdNot(@Param("name") String name, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Branch b SET b.status = :status WHERE b.id IN :ids")
    void updateStatusByIds(@Param("status") BranchStatus status, @Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Branch b SET b.deletedAt = :now WHERE b.id IN :ids")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("ids") Set<Long> ids);

    @Query(value = "SELECT * FROM branches WHERE id IN :ids", nativeQuery = true)
    Set<Branch> findAllDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "UPDATE branches SET deleted_at = NULL WHERE id IN :ids", nativeQuery = true)
    void restoreDeletedByIds(@Param("ids") Set<Long> ids);

}
