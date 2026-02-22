package com.khahnm04.ecommerce.modules.user.repository;

import com.khahnm04.ecommerce.modules.user.entity.Employee;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @EntityGraph(attributePaths = { "user", "branch" })
    Optional<Employee> findById(Long id);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    @Query(value = "SELECT * FROM employees WHERE id = :id", nativeQuery = true)
    Optional<Employee> findDeletedEmployeeById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.deletedAt = :now WHERE e.id IN :ids")
    void softDeleteByIds(LocalDateTime now, Set<Long> ids);

    @Query(value = "SELECT * FROM employees WHERE id IN :ids", nativeQuery = true)
    Set<Employee> findAllDeletedByIds(Set<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "UPDATE employees SET deleted_at = NULL WHERE id IN :ids", nativeQuery = true)
    void restoreDeletedByIds(Set<Long> ids);

}
