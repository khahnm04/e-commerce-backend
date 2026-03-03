package com.khahnm04.ecommerce.modules.inventory.controller;

import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.request.SerialFilterRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.SerialUpdateNotesRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.SerialUpdateStatusRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.response.SerialMovementLogResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.response.SerialNumberResponse;
import com.khahnm04.ecommerce.modules.inventory.service.SerialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/admin/serials")
@RequiredArgsConstructor
public class AdminSerialController {

    private final SerialService serialService;

    // 3.1. Danh sách serial
    @GetMapping
    public ApiResponse<List<SerialNumberResponse>> getAllSerials(
            @Valid @ModelAttribute SerialFilterRequest filter) {
        PageResponse<SerialNumberResponse> pageResponse = serialService.getAllSerials(filter);
        return ApiResponse.<List<SerialNumberResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get all serials successfully")
                .build();
    }

    // 3.2. Chi tiết serial
    @GetMapping("/{id}")
    public ApiResponse<SerialNumberResponse> getSerialById(
            @PathVariable Long id) {
        return ApiResponse.<SerialNumberResponse>builder()
                .data(serialService.getSerialDetailById(id))
                .message("Get serial detail successfully")
                .build();
    }

    // 3.3. Kiểm tra serial theo code
    @GetMapping("/check/{serialCode}")
    public ApiResponse<SerialNumberResponse> checkSerialByCode(
            @PathVariable String serialCode) {
        return ApiResponse.<SerialNumberResponse>builder()
                .data(serialService.checkSerialByCode(serialCode))
                .message("Serial check completed")
                .build();
    }

    // 3.4. Tìm serials theo warehouse
    @GetMapping("/by-warehouse/{warehouseId}")
    public ApiResponse<List<SerialNumberResponse>> getSerialsByWarehouse(
            @PathVariable Long warehouseId) {
        return ApiResponse.<List<SerialNumberResponse>>builder()
                .data(serialService.getSerialsByWarehouse(warehouseId))
                .message("Get serials by warehouse successfully")
                .build();
    }

    // 3.5. Tìm serials theo variant
    @GetMapping("/by-variant/{variantId}")
    public ApiResponse<List<SerialNumberResponse>> getSerialsByVariant(
            @PathVariable Long variantId) {
        return ApiResponse.<List<SerialNumberResponse>>builder()
                .data(serialService.getSerialsByVariant(variantId))
                .message("Get serials by variant successfully")
                .build();
    }

    // 3.6. Cập nhật trạng thái serial
    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateSerialStatus(
            @PathVariable Long id,
            @Valid @RequestBody SerialUpdateStatusRequest request) {
        serialService.updateSerialStatus(id, request);
        return ApiResponse.<Void>builder()
                .message("Serial status updated successfully")
                .build();
    }

    // 3.7. Cập nhật ghi chú serial
    @PatchMapping("/{id}/notes")
    public ApiResponse<Void> updateSerialNotes(
            @PathVariable Long id,
            @Valid @RequestBody SerialUpdateNotesRequest request) {
        serialService.updateSerialNotes(id, request);
        return ApiResponse.<Void>builder()
                .message("Serial notes updated successfully")
                .build();
    }

    // 3.8. Lịch sử di chuyển của serial
    @GetMapping("/{id}/movements")
    public ApiResponse<List<SerialMovementLogResponse>> getSerialMovements(
            @PathVariable Long id) {
        return ApiResponse.<List<SerialMovementLogResponse>>builder()
                .data(serialService.getSerialMovements(id))
                .message("Get serial movements successfully")
                .build();
    }

    // 3.9. Tìm serial available để bán
    @GetMapping("/available")
    public ApiResponse<List<SerialNumberResponse>> getAvailableSerials(
            @RequestParam(required = false) Long productVariantId,
            @RequestParam(required = false) Long warehouseId) {
        return ApiResponse.<List<SerialNumberResponse>>builder()
                .data(serialService.getAvailableSerials(productVariantId, warehouseId))
                .message("Get available serials successfully")
                .build();
    }

    // 3.10. Phân bổ serial cho đơn hàng - TODO: sẽ implement khi có Order service
    @PostMapping("/allocate")
    public ApiResponse<Void> allocateSerial() {
        return ApiResponse.<Void>builder()
                .message("Serial allocation - coming soon")
                .build();
    }

}
