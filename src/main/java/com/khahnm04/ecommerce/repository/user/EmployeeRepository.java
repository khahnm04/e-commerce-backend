package com.khahnm04.ecommerce.repository.user;

import com.khahnm04.ecommerce.entity.user.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    Boolean existsByCodeAndDeletedAtIsNull(String code);

    Boolean existsByCodeAndIdNotAndDeletedAtIsNull(String code, Long id);

    Optional<Employee> findByIdAndDeletedAtIsNull(Long id);

    Optional<Employee> findByIdAndDeletedAtIsNotNull(Long id);

    Optional<Employee> findByEmailAndDeletedAtIsNull(String email);

    @Modifying
    @Query("""
        UPDATE Employee e
        SET e.deletedAt = CURRENT_TIMESTAMP
        WHERE e.id IN :employeeIds
        AND e.deletedAt IS NULL
    """)
    void softDeleteByIds(@Param("employeeIds") Set<Long> employeeIds);

    @Modifying
    @Query("""
        UPDATE Employee e
        SET e.deletedAt = null
        WHERE e.id IN :employeeIds
        AND e.deletedAt IS NOT NULL
    """)
    void restoreByIds(@Param("employeeIds") Set<Long> employeeIds);

    @Modifying
    @Query("DELETE FROM Employee e WHERE e.id IN :ids")
    void deleteAllByIds(Set<Long> ids);

    @Query("""
        SELECT DISTINCT p.code
        FROM EmployeeRole er
        JOIN RolePermission rp ON er.role.id = rp.role.id
        JOIN Permission p ON rp.permission.id = p.id
        WHERE er.employee.email = :email
        AND er.employee.deletedAt IS NULL
        AND er.deletedAt IS NULL
        AND rp.deletedAt IS NULL
        AND p.deletedAt IS NULL
    """)
    List<String> findPermissionCodesByEmail(@Param("email") String email);

    @Query("""
        SELECT COUNT(er) > 0
        FROM EmployeeRole er
        WHERE er.employee.email = :email
        AND er.role.code = 'ADMIN'
        AND er.employee.deletedAt IS NULL
        AND er.deletedAt IS NULL
    """)
    boolean isSuperAdmin(@Param("email") String email);


}
