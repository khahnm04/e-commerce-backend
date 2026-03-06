package com.khahnm04.ecommerce.modules.order.controller;

import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.order.dto.request.OrderFilterRequest;
import com.khahnm04.ecommerce.modules.order.dto.request.UpdateOrderStatusRequest;
import com.khahnm04.ecommerce.modules.order.dto.response.OrderResponse;
import com.khahnm04.ecommerce.modules.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public ApiResponse<List<OrderResponse>> getAllOrders(
            @Valid @ModelAttribute OrderFilterRequest filter
    ) {
        PageResponse<OrderResponse> pageResponse = orderService.getAllOrders(filter);
        return ApiResponse.<List<OrderResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get all orders successfully")
                .build();
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponse> getOrderDetail(
            @PathVariable Long orderId
    ) {
        return ApiResponse.<OrderResponse>builder()
                .data(orderService.getOrderById(orderId))
                .message("Get order detail successfully")
                .build();
    }

    @PutMapping("/{orderId}/status")
    public ApiResponse<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        return ApiResponse.<OrderResponse>builder()
                .data(orderService.updateOrderStatus(orderId, request))
                .message("Order status updated successfully")
                .build();
    }

}
