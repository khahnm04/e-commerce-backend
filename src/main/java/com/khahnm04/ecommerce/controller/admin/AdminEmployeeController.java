package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.config.Translator;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.EmployeeFilterRequest;
import com.khahnm04.ecommerce.dto.request.user.EmployeeRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.user.EmployeeResponse;
import com.khahnm04.ecommerce.service.user.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j(topic = "EMPLOYEE-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/admin/employees")
@RequiredArgsConstructor
@Tag(name = "Admin Employee Controller", description = "APIs for managing employees")
public class AdminEmployeeController {

    private final EmployeeService employeeService;
    private final Translator translator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create employee")
    ApiResponse<EmployeeResponse> createEmployee(
        @Valid @RequestBody EmployeeRequest request
    ) {
        log.info("Create employee");
        return ApiResponse.<EmployeeResponse>builder()
            .data(employeeService.createEmployee(request))
            .message(translator.toLocale("EMPLOYEE_CREATED_SUCCESS"))
            .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee detail")
    ApiResponse<EmployeeResponse> getEmployeeDetailById(
        @PathVariable Long id,
        @RequestParam(defaultValue = "false") boolean deleted
    ) {
        log.info("Get employee detail by id");
        return ApiResponse.<EmployeeResponse>builder()
            .data(employeeService.getEmployeeDetailById(id, deleted))
            .message(translator.toLocale("EMPLOYEE_GET_DETAIL_SUCCESS"))
            .build();
    }

    @GetMapping
    @Operation(summary = "Get all employees")
    PageResponse<EmployeeResponse> getAllEmployees(
        @Valid @ModelAttribute EmployeeFilterRequest request
    ) {
        log.info("Get all employees");
        Page<EmployeeResponse> page = employeeService.getAllEmployees(request);
        return PageResponse.<EmployeeResponse>builder()
            .data(page.getContent())
            .meta(PageResponse.Meta.fromPage(page))
            .message(translator.toLocale("EMPLOYEE_GET_ALL_SUCCESS"))
            .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update employee")
    ApiResponse<EmployeeResponse> updateEmployee(
        @PathVariable Long id,
        @Valid @RequestBody EmployeeRequest request
    ) {
        log.info("Update employee");
        return ApiResponse.<EmployeeResponse>builder()
            .data(employeeService.updateEmployee(id, request))
            .message(translator.toLocale("EMPLOYEE_UPDATED_SUCCESS"))
            .build();
    }

    @PatchMapping("/{id}/soft-delete")
    @Operation(summary = "Soft delete employee")
    ApiResponse<Void> softDeleteEmployee(
        @PathVariable Long id
    ) {
        log.info("Soft delete employee");
        employeeService.softDeleteEmployee(id);
        return ApiResponse.<Void>builder()
            .message(translator.toLocale("EMPLOYEE_SOFT_DELETED_SUCCESS"))
            .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employee")
    ApiResponse<Void> deleteEmployee(
        @PathVariable Long id
    ) {
        log.info("Delete employee");
        employeeService.deleteEmployee(id);
        return ApiResponse.<Void>builder()
            .message(translator.toLocale("EMPLOYEE_DELETED_SUCCESS"))
            .build();
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Restore employee")
    ApiResponse<Void> restoreEmployee(
        @PathVariable Long id
    ) {
        log.info("Restore employee");
        employeeService.restoreEmployee(id);
        return ApiResponse.<Void>builder()
            .message(translator.toLocale("EMPLOYEE_RESTORED_SUCCESS"))
            .build();
    }

    @GetMapping("/me")
    @Operation(summary = "Get my employee profile")
    ApiResponse<EmployeeResponse> getMyProfile() {
        log.info("Get my profile");
        return ApiResponse.<EmployeeResponse>builder()
            .data(employeeService.getMyProfile())
            .message(translator.toLocale("EMPLOYEE_GET_MY_PROFILE_SUCCESS"))
            .build();
    }

    @GetMapping("/me/permissions")
    @Operation(summary = "Get my permission codes")
    ApiResponse<List<String>> getMyPermissions() {
        log.info("Get my permissions");
        return ApiResponse.<List<String>>builder()
            .data(employeeService.getMyPermissions())
            .message(translator.toLocale("EMPLOYEE_GET_MY_PERMISSIONS_SUCCESS"))
            .build();
    }

    @PatchMapping("/bulk")
    @Operation(summary = "Bulk action employees")
    ApiResponse<Void> bulkAction(
        @RequestBody BulkActionRequest request
    ) {
        employeeService.bulkAction(request);
        return ApiResponse.<Void>builder()
            .message(translator.toLocale("EMPLOYEE_BULK_ACTION_SUCCESS"))
            .build();
    }

}
