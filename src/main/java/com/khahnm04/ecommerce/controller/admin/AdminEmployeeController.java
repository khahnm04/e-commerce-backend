package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.request.EmployeeCreationRequest;
import com.khahnm04.ecommerce.dto.request.filter.EmployeeFilterRequest;
import com.khahnm04.ecommerce.dto.request.EmployeeUpdateRequest;
import com.khahnm04.ecommerce.dto.response.EmployeeResponse;
import com.khahnm04.ecommerce.service.EmployeeService;
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
                        @Valid @RequestBody EmployeeCreationRequest request) {
                return ApiResponse.<EmployeeResponse>builder()
                                .data(employeeService.createEmployee(request))
                                .message("Employee created successfully")
                                .build();
        }

        @GetMapping("/{identifier}")
        public ApiResponse<EmployeeResponse> getEmployeeDetail(
                        @PathVariable String identifier,
                        @RequestParam(defaultValue = "false") boolean deleted) {
                return ApiResponse.<EmployeeResponse>builder()
                                .data(employeeService.getEmployeeDetail(identifier, deleted))
                                .message("Get employee detail successfully")
                                .build();
        }

        @GetMapping
        public ApiResponse<List<EmployeeResponse>> getAllEmployees(
                        @Valid @ModelAttribute EmployeeFilterRequest filter) {
                PageResponse<EmployeeResponse> pageResponse = employeeService.getAllEmployees(filter);
                return ApiResponse.<List<EmployeeResponse>>builder()
                                .data(pageResponse.data())
                                .meta(pageResponse.meta())
                                .message("Get all employees successfully")
                                .build();
        }

        @GetMapping("/deleted")
        public ApiResponse<List<EmployeeResponse>> getAllDeletedEmployees(
                        @Valid @ModelAttribute EmployeeFilterRequest filter) {
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
                        @Valid @RequestBody EmployeeUpdateRequest request) {
                return ApiResponse.<EmployeeResponse>builder()
                                .data(employeeService.updateEmployee(id, request))
                                .message("Employee updated successfully")
                                .build();
        }

        @PatchMapping("/{id}/soft-delete")
        public ApiResponse<Void> softDeleteEmployee(
                        @PathVariable Long id) {
                employeeService.softDeleteEmployee(id);
                return ApiResponse.<Void>builder()
                                .message("Employee soft deleted successfully")
                                .build();
        }

        @DeleteMapping("/{id}")
        public ApiResponse<Void> deleteEmployee(
                        @PathVariable Long id) {
                employeeService.deleteEmployee(id);
                return ApiResponse.<Void>builder()
                                .message("Employee deleted successfully")
                                .build();
        }

        @PatchMapping("/{id}/restore")
        public ApiResponse<Void> restoreEmployee(
                        @PathVariable Long id) {
                employeeService.restoreEmployee(id);
                return ApiResponse.<Void>builder()
                                .message("Employee restored successfully")
                                .build();
        }

        @PatchMapping("/bulk")
        public ApiResponse<Void> handleBulkAction(
                        @RequestBody BulkActionRequest request) {
                employeeService.handleBulkAction(request);
                return ApiResponse.<Void>builder()
                                .message("Bulk action executed successfully")
                                .build();
        }

}
