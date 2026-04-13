package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.dto.request.UpdateStockRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.repository.InventoryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/inventory")
@RequiredArgsConstructor
public class AdminInventoryController {

    private final InventoryRepository inventoryRepository;

    @PatchMapping("/{variantId}/add")
    public ApiResponse<Void> addStock(
            @PathVariable Long variantId,
            @Valid @RequestBody UpdateStockRequest request
    ) {
        int quantity = request.quantity();
        inventoryRepository.addQuantity(variantId, quantity);

        log.info("Stock updated for variant ID {}: added {}", variantId, quantity);
        return ApiResponse.<Void>builder()
                .message("Stock updated successfully")
                .build();
    }

}
