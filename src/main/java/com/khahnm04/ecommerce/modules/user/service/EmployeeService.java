package com.khahnm04.ecommerce.modules.user.service;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.user.dto.request.*;
import com.khahnm04.ecommerce.modules.user.dto.response.EmployeeResponse;
import com.khahnm04.ecommerce.modules.user.entity.Employee;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeCreationRequest request);

    Employee getEmployeeById(Long id);

    EmployeeResponse getEmployeeDetail(String identifier, boolean deleted);

    PageResponse<EmployeeResponse> getAllEmployees(EmployeeFilterRequest filter);

    PageResponse<EmployeeResponse> getAllDeletedEmployees(EmployeeFilterRequest filter);

    EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request);

    void updateEmployeeStatus(Long id, EmployeeUpdateStatusRequest status);

    void softDeleteEmployee(Long id);

    void restoreEmployee(Long id);

    void handleBulkAction(EmployeeBulkActionRequest request);

}
