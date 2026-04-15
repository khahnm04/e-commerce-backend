package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.entity.User;
import com.khahnm04.ecommerce.entity.Employee;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    @EntityGraph(attributePaths = { "user" })
    Optional<Employee> findByIdAndDeletedAtIsNull(Long id);

    @EntityGraph(attributePaths = { "user" })
    Optional<Employee> findByIdAndDeletedAtIsNotNull(Long id);

    @EntityGraph(attributePaths = { "user" })
    Optional<Employee> findByCodeAndDeletedAtIsNull(String code);

    @EntityGraph(attributePaths = { "user" })
    Optional<Employee> findByCodeAndDeletedAtIsNotNull(String code);

    @Query("SELECT e FROM Employee e WHERE e.id IN :ids AND e.deletedAt IS NOT NULL")
    Set<Employee> findAllDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.deletedAt = :now, e.deletedBy = :user WHERE e.id IN :ids AND e.deletedAt IS NULL")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("user") User user, @Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.deletedAt = NULL, e.deletedBy = NULL WHERE e.id IN :ids AND e.deletedAt IS NOT NULL")
    void restoreDeletedByIds(@Param("ids") Set<Long> ids);

}
