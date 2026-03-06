package com.khahnm04.ecommerce.modules.branch.repository;

import com.khahnm04.ecommerce.modules.branch.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    boolean existsByCode(String code);

    boolean existsByName(String name);

    Optional<Branch> findByIdAndDeletedAtIsNull(Long id);

    Optional<Branch> findFirstByDeletedAtIsNull();

    Optional<Branch> findByIdAndDeletedAtIsNotNull(Long id);

    Optional<Branch> findByCodeAndDeletedAtIsNull(String code);

    Optional<Branch> findByCodeAndDeletedAtIsNotNull(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    boolean existsByNameAndIdNot(String name, Long id);

    @Query("SELECT b FROM Branch b WHERE b.id IN :ids AND b.deletedAt IS NOT NULL")
    Set<Branch> findAllDeletedByIds(Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Branch b SET b.deletedAt = :now WHERE b.id IN :ids AND b.deletedAt IS NULL")
    void softDeleteByIds(LocalDateTime now, Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Branch b SET b.deletedAt = NULL WHERE b.id IN :ids AND b.deletedAt IS NOT NULL")
    void restoreDeletedByIds(Set<Long> ids);

}
