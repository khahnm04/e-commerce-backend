package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.common.enums.OrderStatus;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.OrderCreationRequest;
import com.khahnm04.ecommerce.dto.request.filter.OrderFilterRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(Long customerId, OrderCreationRequest request);

    PageResponse<OrderResponse> findAll(OrderFilterRequest filter);

    PageResponse<OrderResponse> findAllByCustomerId(Long customerId, OrderFilterRequest filter);

    OrderResponse getById(Long id);

    OrderResponse getByCustomerIdAndId(Long customerId, Long orderId);

    OrderResponse updateStatus(Long id, OrderStatus status);

    void handleBulkAction(BulkActionRequest request);

}
