package com.khahnm04.ecommerce.modules.user.repository;

import com.khahnm04.ecommerce.modules.user.entity.Employee;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    @EntityGraph(attributePaths = { "user", "branch" })
    Optional<Employee> findByIdAndDeletedAtIsNull(Long id);

    @EntityGraph(attributePaths = { "user", "branch" })
    Optional<Employee> findByIdAndDeletedAtIsNotNull(Long id);

    @EntityGraph(attributePaths = { "user", "branch" })
    Optional<Employee> findByCodeAndDeletedAtIsNull(String code);

    @EntityGraph(attributePaths = { "user", "branch" })
    Optional<Employee> findByCodeAndDeletedAtIsNotNull(String code);

    @Query("SELECT e FROM Employee e WHERE e.id IN :ids AND e.deletedAt IS NOT NULL")
    Set<Employee> findAllDeletedByIds(Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.deletedAt = :now WHERE e.id IN :ids AND e.deletedAt IS NULL")
    void softDeleteByIds(LocalDateTime now, Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.deletedAt = NULL WHERE e.id IN :ids AND e.deletedAt IS NOT NULL")
    void restoreDeletedByIds(Set<Long> ids);

}
