package com.khahnm04.ecommerce.modules.inventory.mapper.mybatis;

import com.khahnm04.ecommerce.modules.inventory.dto.response.InventoryCheckDetailResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.response.InventoryCheckResponse;
import com.khahnm04.ecommerce.modules.inventory.enums.InventoryCheckStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface InventoryCheckMyBatisMapper {

    List<InventoryCheckResponse> findAllWithFilterAndSearch(
            @Param("warehouseId") Long warehouseId,
            @Param("checkType") String checkType,
            @Param("status") InventoryCheckStatus status,
            @Param("keyword") String keyword,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("sortColumn") String sortColumn,
            @Param("sortDirection") String sortDirection,
            @Param("offset") long offset,
            @Param("limit") int limit);

    long countWithFilterAndSearch(
            @Param("warehouseId") Long warehouseId,
            @Param("checkType") String checkType,
            @Param("status") InventoryCheckStatus status,
            @Param("keyword") String keyword,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<InventoryCheckDetailResponse> findDetailsByCheckId(@Param("inventoryCheckId") Long inventoryCheckId);

    int countSerialsByWarehouseAndVariant(
            @Param("warehouseId") Long warehouseId,
            @Param("productVariantId") Long productVariantId);

}
