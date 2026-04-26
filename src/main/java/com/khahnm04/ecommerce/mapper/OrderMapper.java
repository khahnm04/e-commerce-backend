package com.khahnm04.ecommerce.mapper;

import com.khahnm04.ecommerce.dto.request.OrderCreationRequest;
import com.khahnm04.ecommerce.dto.response.OrderItemResponse;
import com.khahnm04.ecommerce.dto.response.OrderResponse;
import com.khahnm04.ecommerce.entity.Order;
import com.khahnm04.ecommerce.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface OrderMapper {

    Order toOrder(OrderCreationRequest request);

    @Mapping(target = "customer", source = "customer.fullName")
    @Mapping(target = "estimatedDeliveryDate", source = "expectedDeliveryDate")
    @Mapping(target = "expectedDeliveryDate", source = "expectedDeliveryDate")
    @Mapping(target = "actualDeliveryDate", source = "actualDeliveryDate")
    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "productVariantId", source = "productVariant.id")
    @Mapping(target = "productName", source = "productVariant.name")
    @Mapping(target = "sku", source = "productVariant.sku")
    @Mapping(target = "image", source = "productVariant.image")
    OrderItemResponse toOrderItemResponse(OrderDetail detail);

}
