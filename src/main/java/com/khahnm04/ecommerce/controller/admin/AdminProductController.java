package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.ProductFilterRequest;
import com.khahnm04.ecommerce.dto.request.ProductRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.ProductResponse;
import com.khahnm04.ecommerce.service.ProductService;
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

	@GetMapping("/deleted")
	public ApiResponse<List<ProductResponse>> getAllDeletedProducts(
			@Valid @ModelAttribute ProductFilterRequest filter
    ) {
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
			@Valid @RequestBody ProductRequest request
    ) {
		return ApiResponse.<ProductResponse>builder()
				.data(productService.updateProduct(id, request))
				.message("Product updated successfully")
				.build();
	}

	@PatchMapping("/{id}/soft-delete")
	public ApiResponse<Void> softDeleteProduct(
			@PathVariable Long id
    ) {
		productService.softDeleteProduct(id);
		return ApiResponse.<Void>builder()
				.message("Product soft deleted successfully")
				.build();
	}

	@DeleteMapping("/{id}")
	public ApiResponse<Void> deleteProduct(
			@PathVariable Long id
    ) {
		productService.deleteProduct(id);
		return ApiResponse.<Void>builder()
				.message("Product deleted successfully")
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
			@RequestBody BulkActionRequest request
    ) {
		productService.handleBulkAction(request);
		return ApiResponse.<Void>builder()
				.message("Bulk action executed successfully")
				.build();
	}

}
