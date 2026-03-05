package com.khahnm04.ecommerce.modules.catalog.controller;

import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.catalog.dto.request.AttributeBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.AttributeFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.AttributeRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.AttributeResponse;
import com.khahnm04.ecommerce.modules.catalog.service.AttributeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/attributes")
@RequiredArgsConstructor
public class AdminAttributeController {

	private final AttributeService attributeService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<AttributeResponse> createAttribute(
			@Valid @RequestBody AttributeRequest request
    ) {
		return ApiResponse.<AttributeResponse>builder()
				.data(attributeService.createAttribute(request))
				.message("Attribute created successfully")
				.build();
	}

	@GetMapping("/{identifier}")
	public ApiResponse<AttributeResponse> getAttributeDetail(
			@PathVariable String identifier,
			@RequestParam(defaultValue = "false") boolean deleted
    ) {
		return ApiResponse.<AttributeResponse>builder()
				.data(attributeService.getAttributeDetail(identifier, deleted))
				.message("Get attribute detail successfully")
				.build();
	}

	@GetMapping
	public ApiResponse<List<AttributeResponse>> getAllAttributes(
			@Valid @ModelAttribute AttributeFilterRequest filter
    ) {
		PageResponse<AttributeResponse> pageResponse = attributeService.getAllAttributes(filter);
		return ApiResponse.<List<AttributeResponse>>builder()
				.data(pageResponse.getData())
				.meta(pageResponse.getMeta())
				.message("Get all attributes successfully")
				.build();
	}

	@GetMapping("/deleted")
	public ApiResponse<List<AttributeResponse>> getAllDeletedAttributes(
			@Valid @ModelAttribute AttributeFilterRequest filter
    ) {
		PageResponse<AttributeResponse> pageResponse = attributeService.getAllDeletedAttributes(filter);
		return ApiResponse.<List<AttributeResponse>>builder()
				.data(pageResponse.getData())
				.meta(pageResponse.getMeta())
				.message("Get deleted attributes successfully")
				.build();
	}

	@PutMapping("/{id}")
	public ApiResponse<AttributeResponse> updateAttribute(
			@PathVariable Long id,
			@Valid @RequestBody AttributeRequest request
    ) {
		return ApiResponse.<AttributeResponse>builder()
				.data(attributeService.updateAttribute(id, request))
				.message("Attribute updated successfully")
				.build();
	}

	@DeleteMapping("/{id}")
	public ApiResponse<Void> softDeleteAttribute(
			@PathVariable Long id
    ) {
		attributeService.softDeleteAttribute(id);
		return ApiResponse.<Void>builder()
				.message("Attribute soft deleted successfully")
				.build();
	}

	@PatchMapping("/{id}/restore")
	public ApiResponse<Void> restoreAttribute(
			@PathVariable Long id
    ) {
		attributeService.restoreAttribute(id);
		return ApiResponse.<Void>builder()
				.message("Attribute restored successfully")
				.build();
	}

	@PatchMapping("/bulk")
	public ApiResponse<Void> handleBulkAction(
			@Valid @RequestBody AttributeBulkActionRequest request
    ) {
		attributeService.handleBulkAction(request);
		return ApiResponse.<Void>builder()
				.message("Bulk action executed successfully")
				.build();
	}

}
