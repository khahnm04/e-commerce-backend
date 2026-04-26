package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.entity.Employee;
import com.khahnm04.ecommerce.common.enums.EmployeeStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);

    boolean existsByRoles_Id(@Param("roleId") Long roleId);

    boolean existsByRoles_IdIn(@Param("roleIds") Set<Long> roleIds);

    Optional<Employee> findByPhoneNumber(String phoneNumber);

    Optional<Employee> findByIdAndDeletedAtIsNull(Long id);

    Optional<Employee> findByIdAndDeletedAtIsNotNull(Long id);

    Optional<Employee> findByCodeAndDeletedAtIsNull(String code);

    Optional<Employee> findByCodeAndDeletedAtIsNotNull(String code);

    @Query("SELECT e FROM Employee e WHERE e.email = :identifier OR e.phoneNumber = :identifier")
    Optional<Employee> findByIdentifier(@Param("identifier") String identifier);

    @Query("SELECT e FROM Employee e WHERE e.id IN :ids AND e.deletedAt IS NOT NULL")
    Set<Employee> findAllDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.deletedAt = :now, e.deletedBy = :employee WHERE e.id IN :ids AND e.deletedAt IS NULL")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("employee") Employee employee, @Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.deletedAt = NULL, e.deletedBy = NULL WHERE e.id IN :ids AND e.deletedAt IS NOT NULL")
    void restoreDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.status = :status WHERE e.id IN :ids")
    void updateStatusByIds(@Param("status") EmployeeStatus status, @Param("ids") Set<Long> ids);

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.roles WHERE e.id IN :ids")
    List<Employee> findAllByIdInWithRoles(@Param("ids") Set<Long> ids);

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.roles r LEFT JOIN FETCH r.permissions WHERE e.id = :id")
    Optional<Employee> findByIdWithRolesAndPermissions(@Param("id") Long id);

}
