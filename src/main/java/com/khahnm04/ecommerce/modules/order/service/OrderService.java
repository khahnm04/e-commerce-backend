package com.khahnm04.ecommerce.modules.order.service;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.order.dto.request.CreateOrderRequest;
import com.khahnm04.ecommerce.modules.order.dto.request.OrderFilterRequest;
import com.khahnm04.ecommerce.modules.order.dto.request.UpdateOrderStatusRequest;
import com.khahnm04.ecommerce.modules.order.dto.response.OrderResponse;

public interface OrderService {

    // Customer APIs
    OrderResponse createOrder(Long customerId, CreateOrderRequest request);

    PageResponse<OrderResponse> getCustomerOrders(Long customerId, OrderFilterRequest request);

    OrderResponse getCustomerOrderById(Long customerId, Long orderId);

    void cancelOrder(Long customerId, Long orderId);

    // Admin APIs
    PageResponse<OrderResponse> getAllOrders(OrderFilterRequest request);

    OrderResponse getOrderById(Long orderId);

    OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request);

}
