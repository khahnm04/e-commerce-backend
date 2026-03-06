package com.khahnm04.ecommerce.modules.order.mapper;

import com.khahnm04.ecommerce.modules.order.dto.response.OrderResponse;
import com.khahnm04.ecommerce.modules.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.user.fullName")
    @Mapping(target = "customerPhone", source = "customer.user.phoneNumber")
    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "branchName", source = "branch.name")
    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "paymentMethod", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    OrderResponse toOrderResponse(Order order);

}
