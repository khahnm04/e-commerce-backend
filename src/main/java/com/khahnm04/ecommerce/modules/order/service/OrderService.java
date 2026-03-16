package com.khahnm04.ecommerce.modules.order.service;

import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.modules.order.dto.request.OrderCreationRequest;
import com.khahnm04.ecommerce.modules.order.dto.request.OrderFilterRequest;
import com.khahnm04.ecommerce.modules.order.dto.response.OrderResponse;
import com.khahnm04.ecommerce.modules.order.enums.OrderStatus;
import java.util.Set;

public interface OrderService {

    OrderResponse createOrder(Long customerId, OrderCreationRequest request);

    PageResponse<OrderResponse> findAll(OrderFilterRequest filter);

    PageResponse<OrderResponse> findAllByCustomerId(Long customerId, OrderFilterRequest filter);

    OrderResponse getById(Long id);

    OrderResponse getByCustomerIdAndId(Long customerId, Long orderId);

    OrderResponse updateStatus(Long id, OrderStatus status);

    void handleBulkAction(Set<Long> orderIds, String action);

}
