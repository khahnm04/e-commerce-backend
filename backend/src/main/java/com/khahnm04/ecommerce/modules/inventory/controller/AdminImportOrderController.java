package com.khahnm04.ecommerce.modules.inventory.controller;

import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.request.ImportDetailRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.ImportOrderFilterRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.ImportOrderRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.response.ImportOrderResponse;
import com.khahnm04.ecommerce.modules.inventory.service.ImportOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/admin/import-orders")
@RequiredArgsConstructor
public class AdminImportOrderController {

    private final ImportOrderService importOrderService;

    // 4.1. Tạo phiếu nhập
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ImportOrderResponse> createImportOrder(
            @Valid @RequestBody ImportOrderRequest request) {
        return ApiResponse.<ImportOrderResponse>builder()
                .data(importOrderService.createImportOrder(request))
                .message("Import order created successfully")
                .build();
    }

    // 4.2. Danh sách phiếu nhập
    @GetMapping
    public ApiResponse<List<ImportOrderResponse>> getAllImportOrders(
            @Valid @ModelAttribute ImportOrderFilterRequest filter) {
        PageResponse<ImportOrderResponse> pageResponse = importOrderService.getAllImportOrders(filter);
        return ApiResponse.<List<ImportOrderResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get all import orders successfully")
                .build();
    }

    // 4.3. Chi tiết phiếu nhập
    @GetMapping("/{id}")
    public ApiResponse<ImportOrderResponse> getImportOrderById(
            @PathVariable Long id) {
        return ApiResponse.<ImportOrderResponse>builder()
                .data(importOrderService.getImportOrderDetailById(id))
                .message("Get import order detail successfully")
                .build();
    }

    // 4.4. Cập nhật phiếu nhập
    @PutMapping("/{id}")
    public ApiResponse<ImportOrderResponse> updateImportOrder(
            @PathVariable Long id,
            @Valid @RequestBody ImportOrderRequest request) {
        return ApiResponse.<ImportOrderResponse>builder()
                .data(importOrderService.updateImportOrder(id, request))
                .message("Import order updated successfully")
                .build();
    }

    // 4.5. Thêm serials vào phiếu nhập
    @PostMapping("/{id}/items")
    public ApiResponse<ImportOrderResponse> addItemToImportOrder(
            @PathVariable Long id,
            @Valid @RequestBody ImportDetailRequest request) {
        return ApiResponse.<ImportOrderResponse>builder()
                .data(importOrderService.addItemToImportOrder(id, request))
                .message("Item added to import order successfully")
                .build();
    }

    // 4.6. Xóa serial khỏi phiếu nhập
    @DeleteMapping("/{id}/items/{detailId}")
    public ApiResponse<Void> removeItemFromImportOrder(
            @PathVariable Long id,
            @PathVariable Long detailId) {
        importOrderService.removeItemFromImportOrder(id, detailId);
        return ApiResponse.<Void>builder()
                .message("Item removed from import order successfully")
                .build();
    }

    // 4.7. Hoàn tất phiếu nhập
    @PostMapping("/{id}/complete")
    public ApiResponse<Void> completeImportOrder(
            @PathVariable Long id) {
        importOrderService.completeImportOrder(id);
        return ApiResponse.<Void>builder()
                .message("Import order completed successfully")
                .build();
    }

    // 4.8. Hủy phiếu nhập
    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancelImportOrder(
            @PathVariable Long id) {
        importOrderService.cancelImportOrder(id);
        return ApiResponse.<Void>builder()
                .message("Import order cancelled successfully")
                .build();
    }

    // 4.9. In phiếu nhập - TODO: sẽ implement khi tích hợp thư viện export PDF
    @GetMapping("/{id}/print")
    public ApiResponse<Void> printImportOrder(
            @PathVariable Long id) {
        return ApiResponse.<Void>builder()
                .message("Import order print - coming soon")
                .build();
    }

    // 4.10. Thống kê nhập hàng - TODO: sẽ implement khi có đầy đủ dữ liệu
    @GetMapping("/statistics")
    public ApiResponse<Void> getImportStatistics() {
        return ApiResponse.<Void>builder()
                .message("Import statistics - coming soon")
                .build();
    }

}
