package com.khahnm04.ecommerce.modules.catalog.controller;

import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.ProductUpdateStatusRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.ProductResponse;
import com.khahnm04.ecommerce.modules.catalog.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

	private final ProductService productService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<ProductResponse> createProduct(
			@Valid @RequestBody ProductRequest request
    ) {
		return ApiResponse.<ProductResponse>builder()
				.data(productService.createProduct(request))
				.message("Product created successfully")
				.build();
	}

	@GetMapping("/{identifier}")
	public ApiResponse<ProductResponse> getProductDetail(
			@PathVariable String identifier,
			@RequestParam(defaultValue = "false") boolean deleted
    ) {
		return ApiResponse.<ProductResponse>builder()
				.data(productService.getProductDetail(identifier, deleted))
				.message("Get product detail successfully")
				.build();
	}

	@GetMapping
	public ApiResponse<List<ProductResponse>> getAllProducts(
			@Valid @ModelAttribute ProductFilterRequest filter
    ) {
		PageResponse<ProductResponse> pageResponse = productService.getAllProducts(filter);
		return ApiResponse.<List<ProductResponse>>builder()
				.data(pageResponse.getData())
				.meta(pageResponse.getMeta())
				.message("Get all products successfully")
				.build();
	}

	@GetMapping("/deleted")
	public ApiResponse<List<ProductResponse>> getAllDeletedProducts(
			@Valid @ModelAttribute ProductFilterRequest filter
    ) {
		PageResponse<ProductResponse> pageResponse = productService.getAllDeletedProducts(filter);
		return ApiResponse.<List<ProductResponse>>builder()
				.data(pageResponse.getData())
				.meta(pageResponse.getMeta())
				.message("Get deleted products successfully")
				.build();
	}

	@PutMapping("/{id}")
	public ApiResponse<ProductResponse> updateProduct(
			@PathVariable Long id,
			@Valid @RequestBody ProductRequest request
    ) {
		return ApiResponse.<ProductResponse>builder()
				.data(productService.updateProduct(id, request))
				.message("Product updated successfully")
				.build();
	}

	@PatchMapping("/{id}/status")
	public ApiResponse<Void> updateProductStatus(
			@PathVariable Long id,
			@Valid @RequestBody ProductUpdateStatusRequest request
    ) {
		productService.updateProductStatus(id, request);
		return ApiResponse.<Void>builder()
				.message("Product status updated successfully")
				.build();
	}

	@DeleteMapping("/{id}")
	public ApiResponse<Void> softDeleteProduct(
			@PathVariable Long id
    ) {
		productService.softDeleteProduct(id);
		return ApiResponse.<Void>builder()
				.message("Product soft deleted successfully")
				.build();
	}

	@PatchMapping("/{id}/restore")
	public ApiResponse<Void> restoreProduct(
			@PathVariable Long id
    ) {
		productService.restoreProduct(id);
		return ApiResponse.<Void>builder()
				.message("Product restored successfully")
				.build();
	}

	@PatchMapping("/bulk")
	public ApiResponse<Void> handleBulkAction(
			@Valid @RequestBody ProductBulkActionRequest request
    ) {
		productService.handleBulkAction(request);
		return ApiResponse.<Void>builder()
				.message("Bulk action executed successfully")
				.build();
	}

}
