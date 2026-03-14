package com.khahnm04.ecommerce.modules.user.service.impl;

import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.common.base.entity.BaseEntity;
import com.khahnm04.ecommerce.modules.user.dto.request.EmployeeCreationRequest;
import com.khahnm04.ecommerce.modules.user.dto.request.EmployeeFilterRequest;
import com.khahnm04.ecommerce.modules.user.dto.request.EmployeeUpdateRequest;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.modules.auth.entity.Role;
import com.khahnm04.ecommerce.modules.auth.entity.User;
import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import com.khahnm04.ecommerce.modules.auth.service.UserService;
import com.khahnm04.ecommerce.modules.user.dto.response.EmployeeResponse;
import com.khahnm04.ecommerce.modules.user.entity.Employee;
import com.khahnm04.ecommerce.modules.user.repository.specification.EmployeeSpecification;
import com.khahnm04.ecommerce.modules.user.mapper.EmployeeMapper;
import com.khahnm04.ecommerce.modules.user.repository.EmployeeRepository;
import com.khahnm04.ecommerce.modules.user.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final UserService userService;

    @Override
    @Transactional
    public EmployeeResponse createEmployee(EmployeeCreationRequest request) {
        if (!Objects.equals(request.password(), request.confirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_CONFIRMATION_MISMATCH);
        }
        if (employeeRepository.existsByCode(request.code())) {
            throw new AppException(ErrorCode.EMPLOYEE_CODE_EXISTED);
        }

        User user = userService.createUser(employeeMapper.toUserCreationRequest(request));

        Employee newEmployee = employeeMapper.toEmployee(request);
        newEmployee.setUser(user);
        Employee savedEmployee = employeeRepository.save(newEmployee);

        log.info("Created new employee with code: {}", savedEmployee.getCode());
        return this.buildUserResponse(savedEmployee);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
    }

    @Override
    public EmployeeResponse getEmployeeDetail(String identifier, boolean deleted) {
        Employee employee;

        if (identifier.matches("\\d+")) {
            Long id = Long.parseLong(identifier);
            employee = deleted
                    ? getDeletedEmployeeById(id)
                    : getEmployeeById(id);
        } else {
            employee = deleted
                    ? employeeRepository.findByCodeAndDeletedAtIsNotNull(identifier)
                            .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND))
                    : employeeRepository.findByCodeAndDeletedAtIsNull(identifier)
                            .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        }

        return this.buildUserResponse(employee);
    }

    @Override
    public PageResponse<EmployeeResponse> getAllEmployees(EmployeeFilterRequest request) {
        return getAllWithFilterAndSearch(request, false);
    }

    @Override
    public PageResponse<EmployeeResponse> getAllDeletedEmployees(EmployeeFilterRequest request) {
        return getAllWithFilterAndSearch(request, true);
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request) {
        Employee employee = this.getEmployeeById(id);

        if (StringUtils.hasText(request.code()) &&
                employeeRepository.existsByCodeAndIdNot(request.code(), employee.getId())) {
            throw new AppException(ErrorCode.EMPLOYEE_CODE_EXISTED);
        }

        userService.updateUser(employee.getUser().getId(), employeeMapper.toUserUpdateRequest(request));

        employeeMapper.updateEmployee(employee, request);
        employeeRepository.save(employee);

        log.info("Updated employee with id: {}", id);
        return this.buildUserResponse(employee);
    }

    @Override
    @Transactional
    public void updateEmployeeStatus(Long id, UserStatus status) {
        Employee employee = this.getEmployeeById(id);
        userService.updateUserStatus(employee.getUser().getId(), status);
        log.info("Updated status for employee {} to {}", employee.getCode(), status);
    }

    @Override
    @Transactional
    public void softDeleteEmployee(Long id) {
        Employee employee = this.getEmployeeById(id);
        if (employee.getDeletedAt() != null) {
            log.warn("Employee with id {} is already soft deleted", id);
            return;
        }
        employee.setDeletedAt(LocalDateTime.now());
        employeeRepository.save(employee);
        log.info("Soft-deleted employee with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreEmployee(Long id) {
        Employee employee = getDeletedEmployeeById(id);
        if (employee.getDeletedAt() == null) {
            log.warn("Employee with id {} is not in a deleted state", id);
            return;
        }
        employee.setDeletedAt(null);
        employeeRepository.save(employee);
        log.info("Employee restored with id {}", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(Set<Long> employeeIds, String actionInput) {
        if (employeeIds == null || employeeIds.isEmpty()) {
            throw new AppException(ErrorCode.EMPLOYEE_BULK_ACTION_REQUIRED_IDS);
        }

        String action = actionInput.toUpperCase();

        Set<Employee> existingEmployees = new HashSet<>(
                action.equals("RESTORE")
                        ? employeeRepository.findAllDeletedByIds(employeeIds)
                        : employeeRepository.findAllById(employeeIds));

        Set<Long> existingEmployeeIds = existingEmployees.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        if (employeeIds.size() != existingEmployeeIds.size()) {
            Set<Long> missingIds = new HashSet<>(employeeIds);
            missingIds.removeAll(existingEmployeeIds);
            log.error("Bulk action employees failed. Missing Ids: {}", missingIds);
            throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
        }

        Set<Long> existingUserIds = existingEmployees.stream()
                .map(employee -> employee.getUser().getId())
                .collect(Collectors.toSet());

        switch (action) {
            case "ACTIVE", "INACTIVE", "BANNED", "LOCKED" -> {
                userService.bulkUpdateStatus(existingUserIds, UserStatus.valueOf(action));
                log.info("Bulk update status {} employees ids: {}", action, existingEmployeeIds);
            }
            case "DELETE" -> {
                employeeRepository.softDeleteByIds(LocalDateTime.now(), existingEmployeeIds);
                log.info("Bulk delete employees ids: {}", existingEmployeeIds);
            }
            case "RESTORE" -> {
                employeeRepository.restoreDeletedByIds(existingEmployeeIds);
                log.info("Bulk restore employees ids: {}", existingEmployeeIds);
            }
            default -> throw new AppException(ErrorCode.EMPLOYEE_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} employees", action, existingEmployeeIds.size());
    }

    // ======================== Private methods ========================

    private Employee getDeletedEmployeeById(Long id) {
        return employeeRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
    }

    private EmployeeResponse buildUserResponse(Employee employee) {
        EmployeeResponse employeeResponse = employeeMapper.toResponse(employee);
        Set<Role> roles = employee.getUser().getRoles();
        Set<String> roleNames = roles != null
                ? roles.stream().map(Role::getName).collect(Collectors.toSet())
                : Set.of();
        return employeeResponse.toBuilder().roles(roleNames).build();
    }

    private PageResponse<EmployeeResponse> getAllWithFilterAndSearch(EmployeeFilterRequest request, boolean isDeleted) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(),
                EmployeeSpecification.getSort(request.sort()));

        Specification<Employee> spec = EmployeeSpecification.buildSpecification(request, isDeleted);
        Page<Employee> employeePage = employeeRepository.findAll(spec, pageable);

        List<EmployeeResponse> content = employeePage.getContent().stream()
                .map(employeeMapper::toResponse)
                .collect(Collectors.toList());

        if (!content.isEmpty()) {
            Set<Long> userIds = content.stream()
                    .map(EmployeeResponse::userId)
                    .collect(Collectors.toSet());

            Map<Long, Set<String>> userRolesMap = userService.getRoleNamesByUserIds(userIds);

            content = content.stream()
                    .map(employeeResponse -> employeeResponse.toBuilder()
                            .roles(userRolesMap.getOrDefault(employeeResponse.userId(), Set.of()))
                            .build())
                    .collect(Collectors.toList());
        }

        Page<EmployeeResponse> page = new PageImpl<>(content, pageable, employeePage.getTotalElements());
        return PageResponse.fromPage(page);
    }

}
