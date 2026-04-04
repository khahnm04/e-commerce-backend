package com.khahnm04.ecommerce.service.user;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.request.user.EmployeeCreationRequest;
import com.khahnm04.ecommerce.dto.request.user.EmployeeFilterRequest;
import com.khahnm04.ecommerce.dto.request.user.EmployeeUpdateRequest;
import com.khahnm04.ecommerce.dto.response.user.EmployeeResponse;
import com.khahnm04.ecommerce.entity.user.Employee;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeCreationRequest request);

    Employee getEmployeeById(Long id);

    EmployeeResponse getEmployeeDetail(String identifier, boolean deleted);

    PageResponse<EmployeeResponse> getAllEmployees(EmployeeFilterRequest filter);

    PageResponse<EmployeeResponse> getAllDeletedEmployees(EmployeeFilterRequest filter);

    EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request);

    void softDeleteEmployee(Long id);

    void restoreEmployee(Long id);

    void handleBulkAction(BulkActionRequest request);

}
