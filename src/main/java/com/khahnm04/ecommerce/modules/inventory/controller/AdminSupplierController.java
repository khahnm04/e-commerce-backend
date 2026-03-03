package com.khahnm04.ecommerce.modules.inventory.controller;

import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.request.ImportOrderFilterRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.SupplierBulkActionRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.SupplierFilterRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.SupplierRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.SupplierUpdateStatusRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.response.ImportOrderResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.response.SupplierResponse;
import com.khahnm04.ecommerce.modules.inventory.service.ImportOrderService;
import com.khahnm04.ecommerce.modules.inventory.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/admin/suppliers")
@RequiredArgsConstructor
public class AdminSupplierController {

        private final SupplierService supplierService;
        private final ImportOrderService importOrderService;

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public ApiResponse<SupplierResponse> createSupplier(
                        @Valid @RequestBody SupplierRequest request) {
                return ApiResponse.<SupplierResponse>builder()
                                .data(supplierService.createSupplier(request))
                                .message("Supplier created successfully")
                                .build();
        }

        @GetMapping
        public ApiResponse<List<SupplierResponse>> getAllSuppliers(
                        @Valid @ModelAttribute SupplierFilterRequest filter) {
                PageResponse<SupplierResponse> pageResponse = supplierService.getAllSuppliers(filter);
                return ApiResponse.<List<SupplierResponse>>builder()
                                .data(pageResponse.getData())
                                .meta(pageResponse.getMeta())
                                .message("Get all suppliers successfully")
                                .build();
        }

        @GetMapping("/deleted")
        public ApiResponse<List<SupplierResponse>> getAllDeletedSuppliers(
                        @Valid @ModelAttribute SupplierFilterRequest filter) {
                PageResponse<SupplierResponse> pageResponse = supplierService.getAllDeletedSuppliers(filter);
                return ApiResponse.<List<SupplierResponse>>builder()
                                .data(pageResponse.getData())
                                .meta(pageResponse.getMeta())
                                .message("Get deleted suppliers successfully")
                                .build();
        }

        @GetMapping("/{id}")
        public ApiResponse<SupplierResponse> getSupplierById(
                        @PathVariable Long id) {
                return ApiResponse.<SupplierResponse>builder()
                                .data(supplierService.getSupplierDetailById(id))
                                .message("Get supplier detail successfully")
                                .build();
        }

        @PutMapping("/{id}")
        public ApiResponse<SupplierResponse> updateSupplier(
                        @PathVariable Long id,
                        @Valid @RequestBody SupplierRequest request) {
                return ApiResponse.<SupplierResponse>builder()
                                .data(supplierService.updateSupplier(id, request))
                                .message("Supplier updated successfully")
                                .build();
        }

        @PatchMapping("/{id}/status")
        public ApiResponse<Void> updateSupplierStatus(
                        @PathVariable Long id,
                        @Valid @RequestBody SupplierUpdateStatusRequest request) {
                supplierService.updateSupplierStatus(id, request);
                return ApiResponse.<Void>builder()
                                .message("Supplier status updated successfully")
                                .build();
        }

        @DeleteMapping("/{id}/soft-delete")
        public ApiResponse<Void> softDeleteSupplier(
                        @PathVariable Long id) {
                supplierService.softDeleteSupplier(id);
                return ApiResponse.<Void>builder()
                                .message("Supplier soft deleted successfully")
                                .build();
        }

        @PatchMapping("/{id}/restore")
        public ApiResponse<Void> restoreSupplier(
                        @PathVariable Long id) {
                supplierService.restoreSupplier(id);
                return ApiResponse.<Void>builder()
                                .message("Supplier restored successfully")
                                .build();
        }

        @PatchMapping("/bulk")
        public ApiResponse<Void> handleBulkAction(
                        @Valid @RequestBody SupplierBulkActionRequest request) {
                supplierService.handleBulkAction(request);
                return ApiResponse.<Void>builder()
                                .message("Bulk action executed successfully")
                                .build();
        }

        // 2.10. Lịch sử nhập hàng của supplier
        @GetMapping("/{id}/imports")
        public ApiResponse<List<ImportOrderResponse>> getSupplierImports(
                        @PathVariable Long id,
                        @Valid @ModelAttribute ImportOrderFilterRequest filter) {
                // Verify supplier exists
                supplierService.getSupplierById(id);
                // Reuse import order list with supplierId filter
                filter.setSupplierId(id);
                PageResponse<ImportOrderResponse> pageResponse = importOrderService.getAllImportOrders(filter);
                return ApiResponse.<List<ImportOrderResponse>>builder()
                                .data(pageResponse.getData())
                                .meta(pageResponse.getMeta())
                                .message("Get supplier imports successfully")
                                .build();
        }

}
