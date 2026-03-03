package com.khahnm04.ecommerce.modules.user.controller;

import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.user.dto.request.*;
import com.khahnm04.ecommerce.modules.user.dto.response.CustomerResponse;
import com.khahnm04.ecommerce.modules.user.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/customers")
@RequiredArgsConstructor
public class AdminCustomerController {

    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CustomerResponse> createCustomer(
            @Valid @RequestBody CustomerCreationRequest request
    ) {
        return ApiResponse.<CustomerResponse>builder()
                .data(customerService.createCustomer(request))
                .message("Customer created successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CustomerResponse> getCustomerDetail(
        @PathVariable Long id,
        @RequestParam(defaultValue = "false") boolean deleted
    ) {
        return ApiResponse.<CustomerResponse>builder()
            .data(customerService.getCustomerDetail(id, deleted))
            .message("Get customer detail successfully")
            .build();
    }

    @GetMapping
    public ApiResponse<List<CustomerResponse>> getAllCustomers(
            @Valid @ModelAttribute CustomerFilterRequest filter
    ) {
        PageResponse<CustomerResponse> pageResponse = customerService.getAllCustomers(filter);
        return ApiResponse.<List<CustomerResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get all customers successfully")
                .build();
    }

    @GetMapping("/deleted")
    public ApiResponse<List<CustomerResponse>> getAllDeletedCustomers(
            @Valid @ModelAttribute CustomerFilterRequest filter
    ) {
        PageResponse<CustomerResponse> pageResponse = customerService.getAllDeletedCustomers(filter);
        return ApiResponse.<List<CustomerResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get deleted customers successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CustomerResponse> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerUpdateRequest request
    ) {
        return ApiResponse.<CustomerResponse>builder()
                .data(customerService.updateCustomer(id, request))
                .message("Customer updated successfully")
                .build();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateCustomerStatus(
            @PathVariable Long id,
            @Valid @RequestBody CustomerUpdateStatusRequest status
    ) {
        customerService.updateCustomerStatus(id, status);
        return ApiResponse.<Void>builder()
                .message("Customer status updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDeleteCustomer(
            @PathVariable Long id
    ) {
        customerService.softDeleteCustomer(id);
        return ApiResponse.<Void>builder()
                .message("Customer soft deleted successfully")
                .build();
    }

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restoreCustomer(
            @PathVariable Long id
    ) {
        customerService.restoreCustomer(id);
        return ApiResponse.<Void>builder()
                .message("Customer restored successfully")
                .build();
    }

    @PatchMapping("/bulk")
    public ApiResponse<Void> bulkAction(
            @Valid @RequestBody CustomerBulkActionRequest request
    ) {
        customerService.handleBulkAction(request);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
