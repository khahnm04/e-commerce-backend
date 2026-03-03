package com.khahnm04.ecommerce.modules.catalog.mapper.mybaits;

import com.khahnm04.ecommerce.modules.catalog.dto.response.ProductResponse;
import com.khahnm04.ecommerce.modules.catalog.enums.ProductStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ProductMyBatisMapper {

        List<ProductResponse> findAllWithFilterAndSearch(
                        @Param("keyword") String keyword,
                        @Param("status") ProductStatus status,
                        @Param("brandId") Long brandId,
                        @Param("categoryId") Long categoryId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("isDeleted") boolean isDeleted,
                        @Param("sortColumn") String sortColumn,
                        @Param("sortDirection") String sortDirection,
                        @Param("offset") long offset,
                        @Param("limit") int limit);

        long countWithFilterAndSearch(
                        @Param("keyword") String keyword,
                        @Param("status") ProductStatus status,
                        @Param("brandId") Long brandId,
                        @Param("categoryId") Long categoryId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("isDeleted") boolean isDeleted);

}
