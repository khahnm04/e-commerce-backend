package com.khahnm04.ecommerce.service.order;

import com.khahnm04.ecommerce.common.enums.OrderStatus;
import com.khahnm04.ecommerce.dto.request.order.OrderCreationRequest;
import com.khahnm04.ecommerce.dto.request.order.OrderFilterRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.order.OrderResponse;
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
