package com.khahnm04.ecommerce.modules.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.khahnm04.ecommerce.common.base.dto.ApiResponse;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.modules.user.dto.request.EmployeeCreationRequest;
import com.khahnm04.ecommerce.modules.user.dto.request.EmployeeFilterRequest;
import com.khahnm04.ecommerce.modules.user.dto.request.EmployeeUpdateRequest;
import com.khahnm04.ecommerce.modules.user.dto.response.EmployeeResponse;
import com.khahnm04.ecommerce.modules.user.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/employees")
@RequiredArgsConstructor
public class AdminEmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeCreationRequest request
    ) {
        return ApiResponse.<EmployeeResponse>builder()
                .data(employeeService.createEmployee(request))
                .message("Employee created successfully")
                .build();
    }

    @GetMapping("/{identifier}")
    public ApiResponse<EmployeeResponse> getEmployeeDetail(
            @PathVariable String identifier,
            @RequestParam(defaultValue = "false") boolean deleted
    ) {
        return ApiResponse.<EmployeeResponse>builder()
                .data(employeeService.getEmployeeDetail(identifier, deleted))
                .message("Get employee detail successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<EmployeeResponse>> getAllEmployees(
            @Valid @ModelAttribute EmployeeFilterRequest filter
    ) {
        PageResponse<EmployeeResponse> pageResponse = employeeService.getAllEmployees(filter);
        return ApiResponse.<List<EmployeeResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get all employees successfully")
                .build();
    }

    @GetMapping("/deleted")
    public ApiResponse<List<EmployeeResponse>> getAllDeletedEmployees(
            @Valid @ModelAttribute EmployeeFilterRequest filter
    ) {
        PageResponse<EmployeeResponse> pageResponse = employeeService.getAllDeletedEmployees(filter);
        return ApiResponse.<List<EmployeeResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get deleted employees successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeUpdateRequest request
    ) {
        return ApiResponse.<EmployeeResponse>builder()
                .data(employeeService.updateEmployee(id, request))
                .message("Employee updated successfully")
                .build();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateEmployeeStatus(
            @PathVariable Long id,
            @RequestBody JsonNode request
    ) {
        JsonNode statusNode = request.get("status");
        if (statusNode == null || statusNode.isNull()) {
            throw new AppException(ErrorCode.EMPLOYEE_STATUS_REQUIRED);
        }
        UserStatus status;
        try {
            status = UserStatus.valueOf(statusNode.asText().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.EMPLOYEE_STATUS_INVALID);
        }
        employeeService.updateEmployeeStatus(id, status);
        return ApiResponse.<Void>builder()
                .message("Employee status updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDeleteEmployee(
            @PathVariable Long id
    ) {
        employeeService.softDeleteEmployee(id);
        return ApiResponse.<Void>builder()
                .message("Employee soft deleted successfully")
                .build();
    }

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restoreEmployee(
            @PathVariable Long id
    ) {
        employeeService.restoreEmployee(id);
        return ApiResponse.<Void>builder()
                .message("Employee restored successfully")
                .build();
    }

    @PatchMapping("/bulk")
    public ApiResponse<Void> handleBulkAction (
        @RequestBody JsonNode request
    ) {
        JsonNode employeeIdsNode = request.get("employeeIds");
        if (employeeIdsNode == null || !employeeIdsNode.isArray()) {
            throw new AppException(ErrorCode.EMPLOYEE_SELECTION_REQUIRED);
        }
        Set<Long> employeeIds = new HashSet<>();
        employeeIdsNode.forEach(id -> employeeIds.add(id.asLong()));
        if (employeeIds.isEmpty()) {
            throw new AppException(ErrorCode.EMPLOYEE_SELECTION_REQUIRED);
        }

        JsonNode actionNode = request.get("action");
        if (actionNode == null || actionNode.asText().isBlank()) {
            throw new AppException(ErrorCode.EMPLOYEE_ACTION_REQUIRED);
        }
        String action = actionNode.asText();
        employeeService.handleBulkAction(employeeIds, action);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
