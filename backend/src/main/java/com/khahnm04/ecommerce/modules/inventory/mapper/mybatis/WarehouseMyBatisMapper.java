package com.khahnm04.ecommerce.modules.inventory.mapper.mybatis;

import com.khahnm04.ecommerce.modules.inventory.dto.response.WarehouseResponse;
import com.khahnm04.ecommerce.modules.inventory.enums.WarehouseStatus;
import com.khahnm04.ecommerce.modules.inventory.enums.WarehouseType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface WarehouseMyBatisMapper {

    List<WarehouseResponse> findAllWithFilterAndSearch(
            @Param("branchId") Long branchId,
            @Param("type") WarehouseType type,
            @Param("status") WarehouseStatus status,
            @Param("keyword") String keyword,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("isDeleted") boolean isDeleted,
            @Param("sortColumn") String sortColumn,
            @Param("sortDirection") String sortDirection,
            @Param("offset") long offset,
            @Param("limit") int limit);

    long countWithFilterAndSearch(
            @Param("branchId") Long branchId,
            @Param("type") WarehouseType type,
            @Param("status") WarehouseStatus status,
            @Param("keyword") String keyword,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("isDeleted") boolean isDeleted);

}
