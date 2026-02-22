package com.khahnm04.ecommerce.modules.inventory.mapper.mybatis;

import com.khahnm04.ecommerce.modules.inventory.dto.response.ImportDetailResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.response.ImportOrderResponse;
import com.khahnm04.ecommerce.modules.inventory.enums.ImportOrderStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ImportOrderMyBatisMapper {

    List<ImportOrderResponse> findAllWithFilterAndSearch(
            @Param("warehouseId") Long warehouseId,
            @Param("supplierId") Long supplierId,
            @Param("status") ImportOrderStatus status,
            @Param("keyword") String keyword,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("sortColumn") String sortColumn,
            @Param("sortDirection") String sortDirection,
            @Param("offset") long offset,
            @Param("limit") int limit);

    long countWithFilterAndSearch(
            @Param("warehouseId") Long warehouseId,
            @Param("supplierId") Long supplierId,
            @Param("status") ImportOrderStatus status,
            @Param("keyword") String keyword,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<ImportDetailResponse> findDetailsByImportOrderId(@Param("importOrderId") Long importOrderId);

}
