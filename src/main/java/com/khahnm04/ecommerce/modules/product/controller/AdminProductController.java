package com.khahnm04.ecommerce.modules.product.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.khahnm04.ecommerce.common.base.dto.ApiResponse;
import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.modules.product.dto.request.ProductFilterRequest;
import com.khahnm04.ecommerce.modules.product.dto.request.ProductRequest;
import com.khahnm04.ecommerce.modules.product.dto.response.ProductResponse;
import com.khahnm04.ecommerce.modules.product.enums.ProductStatus;
import com.khahnm04.ecommerce.modules.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

	private final ProductService productService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<ProductResponse> createProduct(
			@Valid @RequestBody ProductRequest request) {
		return ApiResponse.<ProductResponse>builder()
				.data(productService.createProduct(request))
				.message("Product created successfully")
				.build();
	}

	@GetMapping("/{identifier}")
	public ApiResponse<ProductResponse> getProductDetail(
			@PathVariable String identifier,
			@RequestParam(defaultValue = "false") boolean deleted) {
		return ApiResponse.<ProductResponse>builder()
				.data(productService.getProductDetail(identifier, deleted))
				.message("Get product detail successfully")
				.build();
	}

	@GetMapping
	public ApiResponse<List<ProductResponse>> getAllProducts(
			@Valid @ModelAttribute ProductFilterRequest filter) {
		PageResponse<ProductResponse> pageResponse = productService.getAllProducts(filter);
		return ApiResponse.<List<ProductResponse>>builder()
				.data(pageResponse.data())
				.meta(pageResponse.meta())
				.message("Get all products successfully")
				.build();
	}

	@GetMapping("/deleted")
	public ApiResponse<List<ProductResponse>> getAllDeletedProducts(
			@Valid @ModelAttribute ProductFilterRequest filter) {
		PageResponse<ProductResponse> pageResponse = productService.getAllDeletedProducts(filter);
		return ApiResponse.<List<ProductResponse>>builder()
				.data(pageResponse.data())
				.meta(pageResponse.meta())
				.message("Get deleted products successfully")
				.build();
	}

	@PutMapping("/{id}")
	public ApiResponse<ProductResponse> updateProduct(
			@PathVariable Long id,
			@Valid @RequestBody ProductRequest request) {
		return ApiResponse.<ProductResponse>builder()
				.data(productService.updateProduct(id, request))
				.message("Product updated successfully")
				.build();
	}

	@PatchMapping("/{id}/status")
	public ApiResponse<Void> updateProductStatus(
			@PathVariable Long id,
			@RequestBody JsonNode request) {
		JsonNode statusNode = request.get("status");
		if (statusNode == null || statusNode.isNull()) {
			throw new AppException(ErrorCode.PRODUCT_STATUS_REQUIRED);
		}
		ProductStatus status;
		try {
			status = ProductStatus.valueOf(statusNode.asText().toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new AppException(ErrorCode.PRODUCT_STATUS_INVALID);
		}
		productService.updateProductStatus(id, status);
		return ApiResponse.<Void>builder()
				.message("Product status updated successfully")
				.build();
	}

	@DeleteMapping("/{id}")
	public ApiResponse<Void> softDeleteProduct(
			@PathVariable Long id) {
		productService.softDeleteProduct(id);
		return ApiResponse.<Void>builder()
				.message("Product soft deleted successfully")
				.build();
	}

	@PatchMapping("/{id}/restore")
	public ApiResponse<Void> restoreProduct(
			@PathVariable Long id) {
		productService.restoreProduct(id);
		return ApiResponse.<Void>builder()
				.message("Product restored successfully")
				.build();
	}

	@PatchMapping("/bulk")
	public ApiResponse<Void> handleBulkAction(
			@RequestBody JsonNode request) {
		JsonNode productIdsNode = request.get("productIds");
		if (productIdsNode == null || !productIdsNode.isArray()) {
			throw new AppException(ErrorCode.PRODUCT_SELECTION_REQUIRED);
		}
		Set<Long> productIds = new HashSet<>();
		productIdsNode.forEach(node -> productIds.add(node.asLong()));
		if (productIds.isEmpty()) {
			throw new AppException(ErrorCode.PRODUCT_SELECTION_REQUIRED);
		}

		JsonNode actionNode = request.get("action");
		if (actionNode == null || actionNode.asText().isBlank()) {
			throw new AppException(ErrorCode.PRODUCT_ACTION_REQUIRED);
		}
		String action = actionNode.asText();

		productService.handleBulkAction(productIds, action);
		return ApiResponse.<Void>builder()
				.message("Bulk action executed successfully")
				.build();
	}

}
