package com.khahnm04.ecommerce.modules.user.service;

import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import com.khahnm04.ecommerce.modules.user.dto.request.EmployeeCreationRequest;
import com.khahnm04.ecommerce.modules.user.dto.request.EmployeeFilterRequest;
import com.khahnm04.ecommerce.modules.user.dto.request.EmployeeUpdateRequest;
import com.khahnm04.ecommerce.modules.user.dto.response.EmployeeResponse;
import com.khahnm04.ecommerce.modules.user.entity.Employee;

import java.util.Set;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeCreationRequest request);

    Employee getEmployeeById(Long id);

    EmployeeResponse getEmployeeDetail(String identifier, boolean deleted);

    PageResponse<EmployeeResponse> getAllEmployees(EmployeeFilterRequest filter);

    PageResponse<EmployeeResponse> getAllDeletedEmployees(EmployeeFilterRequest filter);

    EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request);

    void updateEmployeeStatus(Long id, UserStatus status);

    void softDeleteEmployee(Long id);

    void restoreEmployee(Long id);

    void handleBulkAction(Set<Long> employeeIds, String action);

}
