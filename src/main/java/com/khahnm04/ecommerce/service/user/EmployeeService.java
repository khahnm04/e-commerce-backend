package com.khahnm04.ecommerce.service.user;

import com.khahnm04.ecommerce.dto.filter.EmployeeFilterRequest;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.user.EmployeeRequest;
import com.khahnm04.ecommerce.dto.response.user.EmployeeResponse;
import com.khahnm04.ecommerce.entity.user.Employee;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeRequest request);

    Employee getEmployeeById(Long id);

    Employee getSoftDeletedEmployeeById(Long id);

    EmployeeResponse getEmployeeDetailById(Long id, boolean isDeleted);

    Page<EmployeeResponse> getAllEmployees(EmployeeFilterRequest filter);

    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);

    void softDeleteEmployee(Long id);

    void deleteEmployee(Long id);

    void restoreEmployee(Long id);

    void bulkAction(BulkActionRequest request);

    EmployeeResponse getMyProfile();

    List<String> getMyPermissions();

}
