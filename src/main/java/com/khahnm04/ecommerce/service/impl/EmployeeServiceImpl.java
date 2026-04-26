package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.common.util.SecurityUtils;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.config.AuditorAwareConfig;
import com.khahnm04.ecommerce.dto.projection.EmployeeProjection;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.EmployeeProfileUpdateRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.request.EmployeeCreationRequest;
import com.khahnm04.ecommerce.dto.request.filter.EmployeeFilterRequest;
import com.khahnm04.ecommerce.dto.request.EmployeeUpdateRequest;
import com.khahnm04.ecommerce.entity.Permission;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.entity.Role;
import com.khahnm04.ecommerce.entity.Employee;
import com.khahnm04.ecommerce.common.enums.EmployeeStatus;
import com.khahnm04.ecommerce.common.enums.Gender;
import com.khahnm04.ecommerce.dto.response.EmployeeResponse;
import com.khahnm04.ecommerce.specification.EmployeeSpecification;
import com.khahnm04.ecommerce.mapper.EmployeeMapper;
import com.khahnm04.ecommerce.repository.EmployeeRepository;
import com.khahnm04.ecommerce.repository.RoleRepository;
import com.khahnm04.ecommerce.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditorAwareConfig auditorAwareConfig;

    @Override
    public EmployeeResponse getProfile() {
        Long employeeId = SecurityUtils.getAuthenticatedUserId();

        Employee employee = employeeRepository.findByIdWithRolesAndPermissions(employeeId)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        return this.buildEmployeeResponse(employee);
    }

    @Override
    @Transactional
    public EmployeeResponse createEmployee(EmployeeCreationRequest request) {
        if (!Objects.equals(request.password(), request.confirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_CONFIRMATION_MISMATCH);
        }
        if (employeeRepository.existsByCode(request.code())) {
            throw new AppException(ErrorCode.EMPLOYEE_CODE_EXISTED);
        }
        if (employeeRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new AppException(ErrorCode.PHONE_NUMBER_EXISTED);
        }
        if (StringUtils.hasText(request.email()) && employeeRepository.existsByEmail(request.email())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        Employee newEmployee = employeeMapper.toEmployee(request);
        newEmployee.setPassword(passwordEncoder.encode(request.password()));
        newEmployee.setStatus(request.status() == null ? EmployeeStatus.ACTIVE : request.status());
        newEmployee.setGender(request.gender() == null ? Gender.OTHER : request.gender());
        assignRolesToEmployee(newEmployee, request.roles());

        Employee savedEmployee = employeeRepository.save(newEmployee);

        log.info("Created new employee with code: {}", savedEmployee.getCode());
        return this.buildEmployeeResponse(savedEmployee);
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

        return this.buildEmployeeResponse(employee);
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
        if (StringUtils.hasText(request.phoneNumber())
                && employeeRepository.existsByPhoneNumberAndIdNot(request.phoneNumber(), id)) {
            throw new AppException(ErrorCode.PHONE_NUMBER_EXISTED);
        }
        if (StringUtils.hasText(request.email()) &&
                employeeRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        employeeMapper.updateEmployee(employee, request);

        if (request.roles() != null) {
            assignRolesToEmployee(employee, request.roles());
        }
        employeeRepository.save(employee);

        log.info("Updated employee with id: {}", id);
        return this.buildEmployeeResponse(employee);
    }

    @Override
    @Transactional
    public EmployeeResponse updateProfile(EmployeeProfileUpdateRequest request) {
        Long employeeId = SecurityUtils.getAuthenticatedUserId();

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        if (StringUtils.hasText(request.phoneNumber())
            && employeeRepository.existsByPhoneNumberAndIdNot(request.phoneNumber(), employee.getId())) {
            throw new AppException(ErrorCode.PHONE_NUMBER_EXISTED);
        }
        if (StringUtils.hasText(request.email()) &&
            employeeRepository.existsByEmailAndIdNot(request.email(), employee.getId())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        employeeMapper.updateEmployeeProfile(employee, request);
        employeeRepository.save(employee);

        log.info("Updated profile for employee with id: {}", employee.getId());
        return this.buildEmployeeResponse(employee);
    }

    @Override
    @Transactional
    public void softDeleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        if (employee.getDeletedAt() != null) {
            log.warn("Employee with id {} is already soft deleted", id);
            return;
        }
        employee.setDeletedAt(LocalDateTime.now());
        employee.setDeletedBy(auditorAwareConfig.getCurrentAuditor().orElse(null));
        employeeRepository.save(employee);
        log.info("Soft-deleted employee with id: {}", id);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = getDeletedEmployeeById(id);
        if (employee.getDeletedAt() == null) {
            log.warn("Employee with id {} is already deleted", id);
            return;
        }
        employeeRepository.delete(employee);
        log.info("Permanently deleted employee with id: {}", id);
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
        employee.setDeletedBy(null);
        employeeRepository.save(employee);
        log.info("Employee restored with id {}", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(BulkActionRequest request) {
        Set<Long> employeeIds = request.ids();
        String action = request.action().toUpperCase();

        if (employeeIds == null || employeeIds.isEmpty()) {
            throw new AppException(ErrorCode.EMPLOYEE_BULK_ACTION_REQUIRED_IDS);
        }

        switch (action) {
            case "ACTIVE", "INACTIVE", "BANNED", "LOCKED" -> {
                employeeRepository.updateStatusByIds(EmployeeStatus.valueOf(action), employeeIds);
                log.info("Bulk update status {} employees ids: {}", action, employeeIds);
            }
            case "SOFT_DELETE" -> {
                Employee currentAuditor = auditorAwareConfig.getCurrentAuditorReference();
                employeeRepository.softDeleteByIds(LocalDateTime.now(), currentAuditor, employeeIds);
                log.info("Bulk soft-delete employees ids: {}", employeeIds);
            }
            case "DELETE" -> {
                Set<Employee> employees = employeeRepository.findAllDeletedByIds(employeeIds);
                employeeRepository.deleteAll(employees);
                log.info("Bulk permanently delete employees ids: {}", employeeIds);
            }
            case "RESTORE" -> {
                employeeRepository.restoreDeletedByIds(employeeIds);
                log.info("Bulk restore employees ids: {}", employeeIds);
            }
            default -> throw new AppException(ErrorCode.EMPLOYEE_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} employees", action, employeeIds.size());
    }

    // ======================== Private methods ========================

    private Employee getDeletedEmployeeById(Long id) {
        return employeeRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
    }

    private EmployeeResponse buildEmployeeResponse(Employee employee) {
        EmployeeResponse employeeResponse = employeeMapper.toResponse(employee);
        Set<Role> roles = employee.getRoles();
        Set<String> roleNames = roles != null
                ? roles.stream().map(Role::getName).collect(Collectors.toSet())
                : Set.of();
        Set<String> permissionCodes = roles != null
                ? roles.stream()
                    .flatMap(role -> role.getPermissions().stream())
                    .map(Permission::getCode)
                    .collect(Collectors.toSet())
                : Set.of();
        return employeeResponse.toBuilder().roles(roleNames).permissions(permissionCodes).build();
    }

    private void assignRolesToEmployee(Employee employee, Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            employee.setRoles(Collections.emptySet());
            return;
        }

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));

        if (roles.size() != roleIds.size()) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
        employee.setRoles(roles);
    }

    private PageResponse<EmployeeResponse> getAllWithFilterAndSearch(EmployeeFilterRequest request, boolean isDeleted) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(), SortUtils.parseSort(request.sort()));

        Specification<Employee> spec = EmployeeSpecification.buildSpecification(request, isDeleted);

        Page<EmployeeProjection> employeePage = employeeRepository
                .findBy(spec, q -> q.as(EmployeeProjection.class).page(pageable));

        Set<Long> employeeIdsInPage = employeePage.getContent().stream()
                .map(EmployeeProjection::getId)
                .collect(Collectors.toSet());

        Map<Long, Set<String>> employeeRolesMap = getRoleNamesByEmployeeIds(employeeIdsInPage);

        List<EmployeeResponse> content = employeePage.getContent().stream()
                .map(proj -> EmployeeResponse.builder()
                        .id(proj.getId())
                        .code(proj.getCode())
                        .fullName(proj.getFullName())
                        .email(proj.getEmail())
                        .phoneNumber(proj.getPhoneNumber())
                        .jobTitle(proj.getJobTitle())
                        .dateOfBirth(proj.getDateOfBirth())
                        .gender(proj.getGender())
                        .avatar(proj.getAvatar())
                        .status(proj.getStatus())
                        .roles(employeeRolesMap.get(proj.getId()))
                        .createdAt(proj.getCreatedAt())
                        .createdBy(proj.getCreatedByFullName())
                        .updatedAt(proj.getUpdatedAt())
                        .updatedBy(proj.getUpdatedByFullName())
                        .deletedAt(proj.getDeletedAt())
                        .deletedBy(proj.getDeletedByFullName())
                        .build())
                .collect(Collectors.toList());

        Page<EmployeeResponse> page = new PageImpl<>(content, pageable, employeePage.getTotalElements());
        return PageResponse.fromPage(page);
    }

    private Map<Long, Set<String>> getRoleNamesByEmployeeIds(Set<Long> employeeIds) {
        if (employeeIds == null || employeeIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return employeeRepository.findAllByIdInWithRoles(employeeIds)
                .stream()
                .collect(Collectors.toMap(
                        Employee::getId,
                        employee -> employee.getRoles() != null
                                ? employee.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
                                : Set.of()));
    }

}
