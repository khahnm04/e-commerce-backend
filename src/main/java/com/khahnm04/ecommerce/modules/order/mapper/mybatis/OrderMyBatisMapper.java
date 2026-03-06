package com.khahnm04.ecommerce.modules.order.mapper.mybatis;

import com.khahnm04.ecommerce.modules.order.dto.response.OrderDetailResponse;
import com.khahnm04.ecommerce.modules.order.dto.response.OrderResponse;
import com.khahnm04.ecommerce.modules.order.enums.OrderStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface OrderMyBatisMapper {

    List<OrderResponse> findAllWithFilterAndSearch(
            @Param("customerId") Long customerId,
            @Param("status") OrderStatus status,
            @Param("keyword") String keyword,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("sortColumn") String sortColumn,
            @Param("sortDirection") String sortDirection,
            @Param("offset") long offset,
            @Param("limit") int limit);

    long countWithFilterAndSearch(
            @Param("customerId") Long customerId,
            @Param("status") OrderStatus status,
            @Param("keyword") String keyword,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<OrderDetailResponse> findDetailsByOrderId(@Param("orderId") Long orderId);

}
