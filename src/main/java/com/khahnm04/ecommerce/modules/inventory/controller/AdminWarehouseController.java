package com.khahnm04.ecommerce.modules.inventory.controller;

import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.request.WarehouseBulkActionRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.WarehouseFilterRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.WarehouseRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.WarehouseUpdateStatusRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.response.WarehouseResponse;
import com.khahnm04.ecommerce.modules.inventory.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/admin/warehouses")
@RequiredArgsConstructor
public class AdminWarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<WarehouseResponse> createWarehouse(
            @Valid @RequestBody WarehouseRequest request
    ) {
        return ApiResponse.<WarehouseResponse>builder()
                .data(warehouseService.createWarehouse(request))
                .message("Warehouse created successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<WarehouseResponse>> getAllWarehouses(
            @Valid @ModelAttribute WarehouseFilterRequest filter
    ) {
        PageResponse<WarehouseResponse> pageResponse = warehouseService.getAllWarehouses(filter);
        return ApiResponse.<List<WarehouseResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get all warehouses successfully")
                .build();
    }

    @GetMapping("/deleted")
    public ApiResponse<List<WarehouseResponse>> getAllDeletedWarehouses(
            @Valid @ModelAttribute WarehouseFilterRequest filter
    ) {
        PageResponse<WarehouseResponse> pageResponse = warehouseService.getAllDeletedWarehouses(filter);
        return ApiResponse.<List<WarehouseResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get deleted warehouses successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<WarehouseResponse> getWarehouseById(
            @PathVariable Long id
    ) {
        return ApiResponse.<WarehouseResponse>builder()
                .data(warehouseService.getWarehouseDetailById(id))
                .message("Get warehouse detail successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<WarehouseResponse> updateWarehouse(
            @PathVariable Long id,
            @Valid @RequestBody WarehouseRequest request) {
        return ApiResponse.<WarehouseResponse>builder()
                .data(warehouseService.updateWarehouse(id, request))
                .message("Warehouse updated successfully")
                .build();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateWarehouseStatus(
            @PathVariable Long id,
            @Valid @RequestBody WarehouseUpdateStatusRequest request) {
        warehouseService.updateWarehouseStatus(id, request);
        return ApiResponse.<Void>builder()
                .message("Warehouse status updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDeleteWarehouse(
            @PathVariable Long id) {
        warehouseService.softDeleteWarehouse(id);
        return ApiResponse.<Void>builder()
                .message("Warehouse soft deleted successfully")
                .build();
    }

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restoreWarehouse(
            @PathVariable Long id) {
        warehouseService.restoreWarehouse(id);
        return ApiResponse.<Void>builder()
                .message("Warehouse restored successfully")
                .build();
    }

    @PatchMapping("/bulk")
    public ApiResponse<Void> handleBulkAction(
            @Valid @RequestBody WarehouseBulkActionRequest request) {
        warehouseService.handleBulkAction(request);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

    @GetMapping("/{id}/inventory")
    public ApiResponse<Void> getWarehouseInventory(
            @PathVariable Long id
    ) {
        return ApiResponse.<Void>builder()
                .message("Warehouse inventory - coming soon")
                .build();
    }

    @GetMapping("/{id}/movements")
    public ApiResponse<Void> getWarehouseMovements(
            @PathVariable Long id
    ) {
        return ApiResponse.<Void>builder()
                .message("Warehouse movements - coming soon")
                .build();
    }

}
