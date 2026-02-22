package com.khahnm04.ecommerce.modules.user.controller;

import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.user.dto.request.*;
import com.khahnm04.ecommerce.modules.user.dto.response.EmployeeResponse;
import com.khahnm04.ecommerce.modules.user.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @GetMapping
    public ApiResponse<List<EmployeeResponse>> getAllEmployees(
            @Valid @ModelAttribute EmployeeFilterRequest filter
    ) {
        PageResponse<EmployeeResponse> pageResponse = employeeService.getAllEmployees(filter);
        return ApiResponse.<List<EmployeeResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get all employees successfully")
                .build();
    }

    @GetMapping("/deleted")
    public ApiResponse<List<EmployeeResponse>> getAllDeletedEmployees(
            @Valid @ModelAttribute EmployeeFilterRequest filter
    ) {
        PageResponse<EmployeeResponse> pageResponse = employeeService.getAllDeletedEmployees(filter);
        return ApiResponse.<List<EmployeeResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get deleted employees successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<EmployeeResponse> getEmployeeById(
            @PathVariable Long id
    ) {
        return ApiResponse.<EmployeeResponse>builder()
                .data(employeeService.getEmployeeDetailById(id))
                .message("Get employee detail successfully")
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
            @Valid @RequestBody EmployeeUpdateStatusRequest status
    ) {
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
    public ApiResponse<Void> bulkAction(
            @Valid @RequestBody EmployeeBulkActionRequest request
    ) {
        employeeService.handleBulkAction(request);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
