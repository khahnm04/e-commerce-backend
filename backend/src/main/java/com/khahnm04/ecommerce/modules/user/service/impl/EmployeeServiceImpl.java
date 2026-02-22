package com.khahnm04.ecommerce.modules.user.service.impl;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.common.entity.BaseEntity;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.auth.entity.Role;
import com.khahnm04.ecommerce.modules.auth.entity.User;
import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import com.khahnm04.ecommerce.modules.auth.service.UserService;
import com.khahnm04.ecommerce.modules.branch.service.BranchService;
import com.khahnm04.ecommerce.modules.user.dto.request.*;
import com.khahnm04.ecommerce.modules.user.dto.response.EmployeeResponse;
import com.khahnm04.ecommerce.modules.user.entity.Employee;
import com.khahnm04.ecommerce.modules.user.enums.EmployeeSortField;
import com.khahnm04.ecommerce.modules.user.mapper.EmployeeMapper;
import com.khahnm04.ecommerce.modules.user.mapper.EmployeeMyBatisMapper;
import com.khahnm04.ecommerce.modules.user.repository.EmployeeRepository;
import com.khahnm04.ecommerce.modules.user.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final EmployeeMyBatisMapper employeeMyBatisMapper;
    private final BranchService branchService;
    private final UserService userService;

    @Override
    @Transactional
    public EmployeeResponse createEmployee(EmployeeCreationRequest request) {
        if (!Objects.equals(request.getPassword(), request.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_CONFIRMATION_MISMATCH);
        }
        if (employeeRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.EMPLOYEE_CODE_EXISTED);
        }

        User user = userService.createUser(employeeMapper.toUserCreationRequest(request));

        Employee newEmployee = employeeMapper.toEmployee(request);
        newEmployee.setUser(user);
        newEmployee.setBranch(branchService.getBranchById(request.getBranchId()));
        Employee savedEmployee = employeeRepository.save(newEmployee);

        log.info("Created new employee with code: {}", savedEmployee.getCode());
        return this.buildUserResponse(savedEmployee);
    }

    @Override
    public PageResponse<EmployeeResponse> getAllEmployees(EmployeeFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<EmployeeResponse> page = this.getAllWithFilterAndSearch(request, pageable, false);
        return PageResponse.fromPage(page);
    }

    @Override
    public PageResponse<EmployeeResponse> getAllDeletedEmployees(EmployeeFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<EmployeeResponse> page = this.getAllWithFilterAndSearch(request, pageable, true);
        return PageResponse.fromPage(page);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
    }

    @Override
    public EmployeeResponse getEmployeeDetailById(Long id) {
        return employeeMapper.toResponse(this.getEmployeeById(id));
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request) {
        Employee employee = this.getEmployeeById(id);

        if (StringUtils.hasText(request.getCode()) &&
                employeeRepository.existsByCodeAndIdNot(request.getCode(), employee.getId())) {
            throw new AppException(ErrorCode.EMPLOYEE_CODE_EXISTED);
        }

        userService.updateUser(employee.getUser().getId(), employeeMapper.toUserUpdateRequest(request));

        employeeMapper.updateEmployee(employee, request);
        if (request.getBranchId() != null) {
            employee.setBranch(branchService.getBranchById(request.getBranchId()));
        }
        employeeRepository.save(employee);

        log.info("Updated employee with id: {}", id);
        return employeeMapper.toResponse(employee);
    }

    @Override
    @Transactional
    public void updateEmployeeStatus(Long id, EmployeeUpdateStatusRequest request) {
        Employee employee = this.getEmployeeById(id);
        userService.updateUserStatus(employee.getUser().getId(), request.getStatus());
        log.info("Updated status for employee {} to {}", employee.getCode(), request.getStatus());
    }

    @Override
    @Transactional
    public void softDeleteEmployee(Long id) {
        Employee employee = this.getEmployeeById(id);
        employee.setDeletedAt(LocalDateTime.now());
        employeeRepository.save(employee);

        userService.softDeleteUser(employee.getUser().getId());
        log.info("Soft deleted employee with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreEmployee(Long id) {
        employeeRepository.findDeletedEmployeeById(id)
                .ifPresentOrElse(employee -> {
                    if (employee.getDeletedAt() == null) {
                        log.warn("Employee with id {} is not deleted, no restoration needed", id);
                        return;
                    }
                    employee.setDeletedAt(null);
                    employeeRepository.save(employee);

                    userService.restoreUser(employee.getUser().getId());
                    log.info("Restored employee id {}", id);
                }, () -> {
                    throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
                });
    }

    @Override
    @Transactional
    public void handleBulkAction(EmployeeBulkActionRequest request) {
        Set<Long> employeeIds = request.getEmployees();
        if (employeeIds == null || employeeIds.isEmpty()) {
            throw new AppException(ErrorCode.EMPLOYEE_BULK_ACTION_REQUIRED_IDS);
        }

        String action = request.getAction().toUpperCase();

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

    private EmployeeResponse buildUserResponse(Employee employee) {
        EmployeeResponse employeeResponse = employeeMapper.toResponse(employee);
        Set<Role> roles = employee.getUser().getRoles();
        Set<String> roleNames = roles != null
                ? roles.stream().map(Role::getName).collect(Collectors.toSet())
                : Set.of();
        employeeResponse.setRoles(roleNames);
        return employeeResponse;
    }

    private Page<EmployeeResponse> getAllWithFilterAndSearch(EmployeeFilterRequest request, Pageable pageable,
            boolean isDeleted) {
        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim()
                : null;

        // Parse sort
        String[] sortParams = SortUtils.parseSortForMyBatis(request.getSort(), EmployeeSortField.class);
        String sortColumn = sortParams[0];
        String sortDirection = sortParams[1];

        List<EmployeeResponse> content = employeeMyBatisMapper.findAllWithFilterAndSearch(
                keyword,
                request.getBranchId(),
                request.getStatus(),
                request.getStartDate(),
                request.getEndDate(),
                isDeleted,
                sortColumn,
                sortDirection,
                pageable.getOffset(),
                pageable.getPageSize());

        if (!content.isEmpty()) {
            Set<Long> userIds = content.stream()
                    .map(EmployeeResponse::getUserId)
                    .collect(Collectors.toSet());

            Map<Long, Set<String>> userRolesMap = userService.getRoleNamesByUserIds(userIds);

            content.forEach(employeeResponse -> employeeResponse
                    .setRoles(userRolesMap.getOrDefault(employeeResponse.getUserId(), Set.of())));
        }

        long total = employeeMyBatisMapper.countWithFilterAndSearch(
                keyword,
                request.getBranchId(),
                request.getStatus(),
                request.getStartDate(),
                request.getEndDate(),
                isDeleted);

        return new PageImpl<>(content, pageable, total);
    }

}
