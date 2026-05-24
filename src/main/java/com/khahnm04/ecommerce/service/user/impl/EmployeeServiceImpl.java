package com.khahnm04.ecommerce.service.user.impl;

import com.khahnm04.ecommerce.dto.request.filter.EmployeeFilterRequest;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.user.EmployeeRequest;
import com.khahnm04.ecommerce.dto.response.user.EmployeeResponse;
import com.khahnm04.ecommerce.entity.user.Employee;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.user.EmployeeMapper;
import com.khahnm04.ecommerce.repository.user.EmployeeRepository;
import com.khahnm04.ecommerce.service.user.EmployeeService;
import com.khahnm04.ecommerce.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j(topic = "EMPLOYEE-SERVICE")
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        return null;
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return null;
    }

    @Override
    public Employee getSoftDeletedEmployeeById(Long id) {
        return null;
    }

    @Override
    public EmployeeResponse getEmployeeDetailById(Long id, boolean isDeleted) {
        return null;
    }

    @Override
    public Page<EmployeeResponse> getAllEmployees(EmployeeFilterRequest filter) {
        return null;
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        return null;
    }

    @Override
    public void softDeleteEmployee(Long id) {

    }

    @Override
    public void deleteEmployee(Long id) {

    }

    @Override
    public void restoreEmployee(Long id) {

    }

    @Override
    public void bulkAction(BulkActionRequest request) {

    }

    @Override
    public EmployeeResponse getMyProfile() {
        String email = SecurityUtil.getCurrentUser();
        Employee employee = employeeRepository.findByEmailAndDeletedAtIsNull(email)
            .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        return employeeMapper.toEmployeeResponse(employee);
    }

    @Override
    public List<String> getMyPermissions() {
        String email = SecurityUtil.getCurrentUser();
        return employeeRepository.findPermissionCodesByEmail(email);
    }

}
