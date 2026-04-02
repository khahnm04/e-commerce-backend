package com.khahnm04.ecommerce.controller.admin.inventory;

import com.fasterxml.jackson.databind.JsonNode;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.repository.inventory.InventoryRepository;
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
            @RequestBody JsonNode request
    ) {
        JsonNode qtyNode = request.get("quantity");
        if (qtyNode == null || qtyNode.isNull()) {
            throw new AppException(ErrorCode.VALIDATION_EXCEPTION);
        }

        int quantity = qtyNode.asInt();
        inventoryRepository.addQuantity(variantId, quantity);

        log.info("Stock updated for variant ID {}: added {}", variantId, quantity);
        return ApiResponse.<Void>builder()
                .message("Stock updated successfully")
                .build();
    }

}
