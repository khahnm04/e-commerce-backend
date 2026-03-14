package com.khahnm04.ecommerce.modules.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.khahnm04.ecommerce.common.base.dto.ApiResponse;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.modules.user.dto.request.CustomerFilterRequest;
import com.khahnm04.ecommerce.modules.user.dto.request.CustomerUpdateRequest;
import com.khahnm04.ecommerce.modules.user.dto.response.CustomerResponse;
import com.khahnm04.ecommerce.modules.user.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/customers")
@RequiredArgsConstructor
public class AdminCustomerController {

	private final CustomerService customerService;

	@GetMapping("/{id}")
	public ApiResponse<CustomerResponse> getCustomerDetail (
			@PathVariable Long id,
			@RequestParam(defaultValue = "false") boolean deleted
    ) {
		return ApiResponse.<CustomerResponse>builder()
				.data(customerService.getCustomerDetail(id, deleted))
				.message("Get customer detail successfully")
				.build();
	}

	@GetMapping
	public ApiResponse<List<CustomerResponse>> getAllCustomers (
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
	public ApiResponse<List<CustomerResponse>> getAllDeletedCustomers (
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
	public ApiResponse<CustomerResponse> updateCustomer (
			@PathVariable Long id,
			@Valid @RequestBody CustomerUpdateRequest request
    ) {
		return ApiResponse.<CustomerResponse>builder()
				.data(customerService.updateCustomer(id, request))
				.message("Customer updated successfully")
				.build();
	}

	@PatchMapping("/{id}/status")
	public ApiResponse<Void> updateCustomerStatus (
			@PathVariable Long id,
			@RequestBody JsonNode request
    ) {
		JsonNode statusNode = request.get("status");
		if (statusNode == null || statusNode.isNull()) {
			throw new AppException(ErrorCode.CUSTOMER_STATUS_REQUIRED);
		}
		UserStatus status;
		try {
			status = UserStatus.valueOf(statusNode.asText().toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new AppException(ErrorCode.CUSTOMER_STATUS_INVALID);
		}
		customerService.updateCustomerStatus(id, status);
		return ApiResponse.<Void>builder()
				.message("Customer status updated successfully")
				.build();
	}

	@DeleteMapping("/{id}")
	public ApiResponse<Void> softDeleteCustomer (
			@PathVariable Long id
    ) {
		customerService.softDeleteCustomer(id);
		return ApiResponse.<Void>builder()
				.message("Customer soft deleted successfully")
				.build();
	}

	@PatchMapping("/{id}/restore")
	public ApiResponse<Void> restoreCustomer (
			@PathVariable Long id
    ) {
		customerService.restoreCustomer(id);
		return ApiResponse.<Void>builder()
				.message("Customer restored successfully")
				.build();
	}

	@PatchMapping("/bulk")
	public ApiResponse<Void> handleBulkAction (
			@RequestBody JsonNode request
    ) {
		JsonNode customerIdsNode = request.get("customerIds");
		if (customerIdsNode == null || !customerIdsNode.isArray()) {
			throw new AppException(ErrorCode.CUSTOMER_SELECTION_REQUIRED);
		}
		Set<Long> customerIds = new HashSet<>();
		customerIdsNode.forEach(node -> customerIds.add(node.asLong()));
		if (customerIds.isEmpty()) {
			throw new AppException(ErrorCode.CUSTOMER_SELECTION_REQUIRED);
		}

		JsonNode actionNode = request.get("action");
		if (actionNode == null || actionNode.asText().isBlank()) {
			throw new AppException(ErrorCode.CUSTOMER_ACTION_REQUIRED);
		}
		String action = actionNode.asText();

		customerService.handleBulkAction(customerIds, action);
		return ApiResponse.<Void>builder()
				.message("Bulk action executed successfully")
				.build();
	}

}
