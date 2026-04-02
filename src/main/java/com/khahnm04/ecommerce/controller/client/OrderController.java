package com.khahnm04.ecommerce.controller.client;

import com.khahnm04.ecommerce.dto.request.order.OrderCreationRequest;
import com.khahnm04.ecommerce.dto.request.order.OrderFilterRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.order.OrderResponse;
import com.khahnm04.ecommerce.service.order.OrderService;
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
            @Valid @RequestBody OrderCreationRequest request
    ) {
        log.info("Creating order for customer: {}", customerId);
        return ApiResponse.<OrderResponse>builder()
                .data(orderService.createOrder(customerId, request))
                .message("Order created successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<OrderResponse>> getCustomerOrders(
            @PathVariable Long customerId,
            @Valid @ModelAttribute OrderFilterRequest filter
    ) {
        log.info("Fetching orders for customer: {}", customerId);
        PageResponse<OrderResponse> pageResponse = orderService.findAllByCustomerId(customerId, filter);
        return ApiResponse.<List<OrderResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get customer orders successful")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> getCustomerOrderDetail(
            @PathVariable Long customerId,
            @PathVariable Long id
    ) {
        log.info("Fetching order detail {} for customer: {}", id, customerId);
        return ApiResponse.<OrderResponse>builder()
                .data(orderService.getByCustomerIdAndId(customerId, id))
                .message("Get order detail successful")
                .build();
    }

}
