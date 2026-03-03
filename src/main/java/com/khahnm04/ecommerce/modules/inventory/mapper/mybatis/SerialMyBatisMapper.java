package com.khahnm04.ecommerce.modules.inventory.mapper.mybatis;

import com.khahnm04.ecommerce.modules.inventory.dto.response.SerialMovementLogResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.response.SerialNumberResponse;
import com.khahnm04.ecommerce.modules.inventory.enums.SerialNumberStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface SerialMyBatisMapper {

    List<SerialNumberResponse> findAllWithFilterAndSearch(
            @Param("productVariantId") Long productVariantId,
            @Param("warehouseId") Long warehouseId,
            @Param("status") SerialNumberStatus status,
            @Param("keyword") String keyword,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("sortColumn") String sortColumn,
            @Param("sortDirection") String sortDirection,
            @Param("offset") long offset,
            @Param("limit") int limit);

    long countWithFilterAndSearch(
            @Param("productVariantId") Long productVariantId,
            @Param("warehouseId") Long warehouseId,
            @Param("status") SerialNumberStatus status,
            @Param("keyword") String keyword,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<SerialMovementLogResponse> findMovementsBySerialId(@Param("serialNumberId") Long serialNumberId);

}
