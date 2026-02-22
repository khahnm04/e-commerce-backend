package com.khahnm04.ecommerce.modules.inventory.controller;

import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.request.TransferDetailRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.TransferOrderFilterRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.TransferOrderRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.response.TransferOrderResponse;
import com.khahnm04.ecommerce.modules.inventory.service.TransferOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/admin/transfer-orders")
@RequiredArgsConstructor
public class AdminTransferOrderController {

    private final TransferOrderService transferOrderService;

    // 5.1. Tạo phiếu điều chuyển
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TransferOrderResponse> createTransferOrder(
            @Valid @RequestBody TransferOrderRequest request) {
        return ApiResponse.<TransferOrderResponse>builder()
                .data(transferOrderService.createTransferOrder(request))
                .message("Transfer order created successfully")
                .build();
    }

    // 5.2. Danh sách phiếu điều chuyển
    @GetMapping
    public ApiResponse<List<TransferOrderResponse>> getAllTransferOrders(
            @Valid @ModelAttribute TransferOrderFilterRequest filter) {
        PageResponse<TransferOrderResponse> pageResponse = transferOrderService.getAllTransferOrders(filter);
        return ApiResponse.<List<TransferOrderResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get all transfer orders successfully")
                .build();
    }

    // 5.3. Chi tiết phiếu điều chuyển
    @GetMapping("/{id}")
    public ApiResponse<TransferOrderResponse> getTransferOrderById(
            @PathVariable Long id) {
        return ApiResponse.<TransferOrderResponse>builder()
                .data(transferOrderService.getTransferOrderDetailById(id))
                .message("Get transfer order detail successfully")
                .build();
    }

    // 5.4. Cập nhật phiếu điều chuyển
    @PutMapping("/{id}")
    public ApiResponse<TransferOrderResponse> updateTransferOrder(
            @PathVariable Long id,
            @Valid @RequestBody TransferOrderRequest request) {
        return ApiResponse.<TransferOrderResponse>builder()
                .data(transferOrderService.updateTransferOrder(id, request))
                .message("Transfer order updated successfully")
                .build();
    }

    // 5.5. Thêm serials vào phiếu chuyển
    @PostMapping("/{id}/items")
    public ApiResponse<TransferOrderResponse> addItemToTransferOrder(
            @PathVariable Long id,
            @Valid @RequestBody TransferDetailRequest request) {
        return ApiResponse.<TransferOrderResponse>builder()
                .data(transferOrderService.addItemToTransferOrder(id, request))
                .message("Item added to transfer order successfully")
                .build();
    }

    // 5.6. Xóa serial khỏi phiếu chuyển
    @DeleteMapping("/{id}/items/{detailId}")
    public ApiResponse<Void> removeItemFromTransferOrder(
            @PathVariable Long id,
            @PathVariable Long detailId) {
        transferOrderService.removeItemFromTransferOrder(id, detailId);
        return ApiResponse.<Void>builder()
                .message("Item removed from transfer order successfully")
                .build();
    }

    // 5.7. Bắt đầu vận chuyển
    @PostMapping("/{id}/start-shipping")
    public ApiResponse<Void> startShipping(
            @PathVariable Long id) {
        transferOrderService.startShipping(id);
        return ApiResponse.<Void>builder()
                .message("Transfer order shipping started")
                .build();
    }

    // 5.8. Hoàn tất điều chuyển
    @PostMapping("/{id}/complete")
    public ApiResponse<Void> completeTransferOrder(
            @PathVariable Long id) {
        transferOrderService.completeTransferOrder(id);
        return ApiResponse.<Void>builder()
                .message("Transfer order completed successfully")
                .build();
    }

    // 5.9. Hủy phiếu chuyển
    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancelTransferOrder(
            @PathVariable Long id) {
        transferOrderService.cancelTransferOrder(id);
        return ApiResponse.<Void>builder()
                .message("Transfer order cancelled successfully")
                .build();
    }

    // 5.10. In phiếu điều chuyển - TODO: sẽ implement khi tích hợp thư viện export
    // PDF
    @GetMapping("/{id}/print")
    public ApiResponse<Void> printTransferOrder(
            @PathVariable Long id) {
        return ApiResponse.<Void>builder()
                .message("Transfer order print - coming soon")
                .build();
    }

    // 5.11. Thống kê điều chuyển - TODO: sẽ implement khi có đầy đủ dữ liệu
    @GetMapping("/statistics")
    public ApiResponse<Void> getTransferStatistics() {
        return ApiResponse.<Void>builder()
                .message("Transfer statistics - coming soon")
                .build();
    }

}
