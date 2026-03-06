package com.khahnm04.ecommerce.modules.order.controller;

import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.order.dto.request.CreateOrderRequest;
import com.khahnm04.ecommerce.modules.order.dto.request.OrderFilterRequest;
import com.khahnm04.ecommerce.modules.order.dto.response.OrderResponse;
import com.khahnm04.ecommerce.modules.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/customers/{customerId}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<OrderResponse> createOrder(
            @PathVariable Long customerId,
            @Valid @RequestBody CreateOrderRequest request
    ) {
        return ApiResponse.<OrderResponse>builder()
                .data(orderService.createOrder(customerId, request))
                .message("Order created successfully")
                .build();
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponse> getOrderDetail(
        @PathVariable Long customerId,
        @PathVariable Long orderId
    ) {
        return ApiResponse.<OrderResponse>builder()
            .data(orderService.getCustomerOrderById(customerId, orderId))
            .message("Get order detail successfully")
            .build();
    }

    @GetMapping
    public ApiResponse<List<OrderResponse>> getMyOrders(
            @PathVariable Long customerId,
            @Valid @ModelAttribute OrderFilterRequest filter
    ) {
        PageResponse<OrderResponse> pageResponse = orderService.getCustomerOrders(customerId, filter);
        return ApiResponse.<List<OrderResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get orders successfully")
                .build();
    }

    @PutMapping("/{orderId}/cancel")
    public ApiResponse<Void> cancelOrder(
            @PathVariable Long customerId,
            @PathVariable Long orderId
    ) {
        orderService.cancelOrder(customerId, orderId);
        return ApiResponse.<Void>builder()
                .message("Order cancelled successfully")
                .build();
    }

}
