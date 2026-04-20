package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.dto.request.CustomerCreationRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.request.filter.CustomerFilterRequest;
import com.khahnm04.ecommerce.dto.request.CustomerUpdateRequest;
import com.khahnm04.ecommerce.dto.response.CustomerResponse;
import com.khahnm04.ecommerce.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/customers")
@RequiredArgsConstructor
public class AdminCustomerController {

    private final CustomerService customerService;

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

    @PostMapping
    public ApiResponse<CustomerResponse> createCustomer(
            @Valid @RequestBody CustomerCreationRequest request
    ) {
        return ApiResponse.<CustomerResponse>builder()
                .data(customerService.createCustomer(request))
                .message("Customer created successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<CustomerResponse>> getAllCustomers(
            @Valid @ModelAttribute CustomerFilterRequest filter
    ) {
        PageResponse<CustomerResponse> pageResponse = customerService.getAllCustomers(filter);
        return ApiResponse.<List<CustomerResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get all customers successfully")
                .build();
    }

    @GetMapping("/deleted")
    public ApiResponse<List<CustomerResponse>> getAllDeletedCustomers(
            @Valid @ModelAttribute CustomerFilterRequest filter
    ) {
        PageResponse<CustomerResponse> pageResponse = customerService.getAllDeletedCustomers(filter);
        return ApiResponse.<List<CustomerResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
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

    @PatchMapping("/{id}/soft-delete")
    public ApiResponse<Void> softDeleteCustomer(
            @PathVariable Long id
    ) {
        customerService.softDeleteCustomer(id);
        return ApiResponse.<Void>builder()
                .message("Customer soft deleted successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCustomer(
            @PathVariable Long id
    ) {
        customerService.deleteCustomer(id);
        return ApiResponse.<Void>builder()
                .message("Customer deleted successfully")
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
    public ApiResponse<Void> handleBulkAction(
            @RequestBody BulkActionRequest request
    ) {
        customerService.handleBulkAction(request);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
