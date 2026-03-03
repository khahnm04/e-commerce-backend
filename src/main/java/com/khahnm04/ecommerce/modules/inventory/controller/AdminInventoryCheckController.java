package com.khahnm04.ecommerce.modules.inventory.controller;

import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.request.InventoryCheckBatchUpdateRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.InventoryCheckDetailUpdateRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.InventoryCheckFilterRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.InventoryCheckRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.response.InventoryCheckResponse;
import com.khahnm04.ecommerce.modules.inventory.service.InventoryCheckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/admin/inventory-checks")
@RequiredArgsConstructor
public class AdminInventoryCheckController {

    private final InventoryCheckService inventoryCheckService;

    // 6.1. Tạo phiếu kiểm kê
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<InventoryCheckResponse> createInventoryCheck(
            @Valid @RequestBody InventoryCheckRequest request) {
        return ApiResponse.<InventoryCheckResponse>builder()
                .data(inventoryCheckService.createInventoryCheck(request))
                .message("Inventory check created successfully")
                .build();
    }

    // 6.2. Danh sách phiếu kiểm kê
    @GetMapping
    public ApiResponse<List<InventoryCheckResponse>> getAllInventoryChecks(
            @Valid @ModelAttribute InventoryCheckFilterRequest filter) {
        PageResponse<InventoryCheckResponse> pageResponse = inventoryCheckService.getAllInventoryChecks(filter);
        return ApiResponse.<List<InventoryCheckResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get all inventory checks successfully")
                .build();
    }

    // 6.3. Chi tiết phiếu kiểm kê
    @GetMapping("/{id}")
    public ApiResponse<InventoryCheckResponse> getInventoryCheckById(
            @PathVariable Long id) {
        return ApiResponse.<InventoryCheckResponse>builder()
                .data(inventoryCheckService.getInventoryCheckDetailById(id))
                .message("Get inventory check detail successfully")
                .build();
    }

    // 6.4. Cập nhật phiếu kiểm kê
    @PutMapping("/{id}")
    public ApiResponse<InventoryCheckResponse> updateInventoryCheck(
            @PathVariable Long id,
            @Valid @RequestBody InventoryCheckRequest request) {
        return ApiResponse.<InventoryCheckResponse>builder()
                .data(inventoryCheckService.updateInventoryCheck(id, request))
                .message("Inventory check updated successfully")
                .build();
    }

    // 6.5. Cập nhật số lượng thực tế
    @PatchMapping("/{id}/details/{detailId}")
    public ApiResponse<InventoryCheckResponse> updateDetailActualQuantity(
            @PathVariable Long id,
            @PathVariable Long detailId,
            @Valid @RequestBody InventoryCheckDetailUpdateRequest request) {
        return ApiResponse.<InventoryCheckResponse>builder()
                .data(inventoryCheckService.updateDetailActualQuantity(id, detailId, request))
                .message("Detail actual quantity updated successfully")
                .build();
    }

    // 6.6. Cập nhật hàng loạt số lượng
    @PatchMapping("/{id}/details/batch")
    public ApiResponse<InventoryCheckResponse> batchUpdateDetails(
            @PathVariable Long id,
            @Valid @RequestBody InventoryCheckBatchUpdateRequest request) {
        return ApiResponse.<InventoryCheckResponse>builder()
                .data(inventoryCheckService.batchUpdateDetails(id, request))
                .message("Batch update details successfully")
                .build();
    }

    // 6.7. Hoàn tất kiểm kê
    @PostMapping("/{id}/complete")
    public ApiResponse<Void> completeInventoryCheck(
            @PathVariable Long id) {
        inventoryCheckService.completeInventoryCheck(id);
        return ApiResponse.<Void>builder()
                .message("Inventory check completed successfully")
                .build();
    }

    // 6.8. Hủy kiểm kê
    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancelInventoryCheck(
            @PathVariable Long id) {
        inventoryCheckService.cancelInventoryCheck(id);
        return ApiResponse.<Void>builder()
                .message("Inventory check cancelled successfully")
                .build();
    }

    // 6.9. In phiếu kiểm kê - TODO: sẽ implement khi tích hợp thư viện export PDF
    @GetMapping("/{id}/print")
    public ApiResponse<Void> printInventoryCheck(
            @PathVariable Long id) {
        return ApiResponse.<Void>builder()
                .message("Inventory check print - coming soon")
                .build();
    }

    // 6.10. Thống kê kiểm kê - TODO: sẽ implement khi có đầy đủ dữ liệu
    @GetMapping("/statistics")
    public ApiResponse<Void> getInventoryCheckStatistics() {
        return ApiResponse.<Void>builder()
                .message("Inventory check statistics - coming soon")
                .build();
    }

}
