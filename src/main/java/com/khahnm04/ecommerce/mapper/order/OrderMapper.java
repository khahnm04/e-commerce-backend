package com.khahnm04.ecommerce.mapper.order;

import com.khahnm04.ecommerce.dto.request.order.OrderCreationRequest;
import com.khahnm04.ecommerce.dto.response.order.OrderItemResponse;
import com.khahnm04.ecommerce.dto.response.order.OrderResponse;
import com.khahnm04.ecommerce.entity.order.Order;
import com.khahnm04.ecommerce.entity.order.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "code", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "coupon", ignore = true)
    @Mapping(target = "subTotal", ignore = true)
    @Mapping(target = "discount", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "expectedDeliveryDate", ignore = true)
    @Mapping(target = "actualDeliveryDate", ignore = true)
    Order toOrder(OrderCreationRequest request);

    @Mapping(target = "items", ignore = true)
    @Mapping(target = "paymentUrl", ignore = true)
    @Mapping(target = "totalQuantity", ignore = true)
    @Mapping(target = "paymentMethod", ignore = true)
    @Mapping(target = "customer", source = "customer.user.fullName")
    @Mapping(target = "estimatedDeliveryDate", source = "expectedDeliveryDate")
    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "productVariantId", source = "productVariant.id")
    @Mapping(target = "productName", source = "productVariant.name")
    @Mapping(target = "sku", source = "productVariant.sku")
    @Mapping(target = "image", source = "productVariant.image")
    OrderItemResponse toOrderItemResponse(OrderDetail detail);

}
