package com.khahnm04.ecommerce.modules.order.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.khahnm04.ecommerce.common.base.dto.ApiResponse;
import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.modules.order.dto.request.OrderFilterRequest;
import com.khahnm04.ecommerce.modules.order.dto.response.OrderResponse;
import com.khahnm04.ecommerce.modules.order.enums.OrderStatus;
import com.khahnm04.ecommerce.modules.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public ApiResponse<List<OrderResponse>> getAllOrders(
            @Valid @ModelAttribute OrderFilterRequest filter
    ) {
        log.info("Admin fetching all orders with filter: {}", filter);
        PageResponse<OrderResponse> pageResponse = orderService.findAll(filter);
        return ApiResponse.<List<OrderResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get all orders successful")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> getOrderDetail(
        @PathVariable Long id
    ) {
        log.info("Admin fetching order detail: {}", id);
        return ApiResponse.<OrderResponse>builder()
                .data(orderService.getById(id))
                .message("Get order detail successful")
                .build();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody JsonNode request
    ) {
        JsonNode statusNode = request.get("status");
        if (statusNode == null || statusNode.isNull()) {
            throw new AppException(ErrorCode.ORDER_STATUS_REQUIRED);
        }
        OrderStatus status;
        try {
            status = OrderStatus.valueOf(statusNode.asText().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.ORDER_INVALID_STATUS_TRANSITION); // Reuse or use a generic one
        }
        log.info("Admin updating order {} status to {}", id, status);
        return ApiResponse.<OrderResponse>builder()
                .data(orderService.updateStatus(id, status))
                .message("Order status updated successful")
                .build();
    }

    @PatchMapping("/bulk")
    public ApiResponse<Void> handleBulkAction(
            @RequestBody JsonNode request
    ) {
        JsonNode orderIdsNode = request.get("orderIds");
        if (orderIdsNode == null || !orderIdsNode.isArray()) {
            throw new AppException(ErrorCode.ORDER_SELECTION_REQUIRED);
        }
        Set<Long> orderIds = new HashSet<>();
        orderIdsNode.forEach(id -> orderIds.add(id.asLong()));
        if (orderIds.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_SELECTION_REQUIRED);
        }

        JsonNode actionNode = request.get("action");
        if (actionNode == null || actionNode.asText().isBlank()) {
            throw new AppException(ErrorCode.ORDER_ACTION_REQUIRED);
        }
        String action = actionNode.asText();
        log.info("Admin executing bulk action {} for orders: {}", action, orderIds);
        orderService.handleBulkAction(orderIds, action);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
