package com.khahnm04.ecommerce.modules.inventory.mapper.mybatis;

import com.khahnm04.ecommerce.modules.inventory.dto.response.TransferDetailResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.response.TransferOrderResponse;
import com.khahnm04.ecommerce.modules.inventory.enums.TransferOrderStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TransferOrderMyBatisMapper {

    List<TransferOrderResponse> findAllWithFilterAndSearch(
            @Param("fromWarehouseId") Long fromWarehouseId,
            @Param("toWarehouseId") Long toWarehouseId,
            @Param("status") TransferOrderStatus status,
            @Param("keyword") String keyword,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("sortColumn") String sortColumn,
            @Param("sortDirection") String sortDirection,
            @Param("offset") long offset,
            @Param("limit") int limit);

    long countWithFilterAndSearch(
            @Param("fromWarehouseId") Long fromWarehouseId,
            @Param("toWarehouseId") Long toWarehouseId,
            @Param("status") TransferOrderStatus status,
            @Param("keyword") String keyword,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<TransferDetailResponse> findDetailsByTransferOrderId(@Param("transferOrderId") Long transferOrderId);

}
