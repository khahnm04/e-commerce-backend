package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.EmployeeProfileUpdateRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.request.EmployeeCreationRequest;
import com.khahnm04.ecommerce.dto.request.filter.EmployeeFilterRequest;
import com.khahnm04.ecommerce.dto.request.EmployeeUpdateRequest;
import com.khahnm04.ecommerce.dto.response.EmployeeResponse;
import com.khahnm04.ecommerce.entity.Employee;

public interface EmployeeService {

    EmployeeResponse getProfile();

    EmployeeResponse createEmployee(EmployeeCreationRequest request);

    Employee getEmployeeById(Long id);

    EmployeeResponse getEmployeeDetail(String identifier, boolean deleted);

    PageResponse<EmployeeResponse> getAllEmployees(EmployeeFilterRequest filter);

    PageResponse<EmployeeResponse> getAllDeletedEmployees(EmployeeFilterRequest filter);

    EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request);

    EmployeeResponse updateProfile(EmployeeProfileUpdateRequest request);

    void softDeleteEmployee(Long id);

    void deleteEmployee(Long id);

    void restoreEmployee(Long id);

    void handleBulkAction(BulkActionRequest request);

}
