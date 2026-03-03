package com.khahnm04.ecommerce.modules.catalog.mapper.mybaits;

import com.khahnm04.ecommerce.modules.catalog.dto.response.VariantValueResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface VariantValueMyBatisMapper {

        List<VariantValueResponse> findAllWithFilterAndSearch(
                        @Param("variantId") Long variantId,
                        @Param("keyword") String keyword,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("isDeleted") boolean isDeleted,
                        @Param("sortColumn") String sortColumn,
                        @Param("sortDirection") String sortDirection,
                        @Param("offset") long offset,
                        @Param("limit") int limit);

        long countWithFilterAndSearch(
                        @Param("variantId") Long variantId,
                        @Param("keyword") String keyword,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("isDeleted") boolean isDeleted);

}
