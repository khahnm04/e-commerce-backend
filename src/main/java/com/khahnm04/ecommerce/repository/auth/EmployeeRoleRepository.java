package com.khahnm04.ecommerce.repository.auth;

import com.khahnm04.ecommerce.entity.auth.EmployeeRole;
import com.khahnm04.ecommerce.entity.user.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployeeRoleRepository extends JpaRepository<EmployeeRole, Long> {

    @Query("""
        SELECT er FROM EmployeeRole er
        JOIN FETCH er.role
        WHERE er.employee = :employee
    """)
    List<EmployeeRole> findByEmployeeWithRole(@Param("employee") Employee employee);

    Boolean existsByEmployeeIdAndRoleId(Long id, Long id1);

}