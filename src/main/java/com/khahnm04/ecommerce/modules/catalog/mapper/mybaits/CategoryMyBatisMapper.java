package com.khahnm04.ecommerce.modules.catalog.mapper.mybaits;

import com.khahnm04.ecommerce.modules.catalog.dto.response.CategoryResponse;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface CategoryMyBatisMapper {

        /**
         * Search and filter categories with pagination.
         *
         * @param keyword       Search keyword (matches name, slug)
         * @param parentId      Filter by parent category id
         * @param startDate     Filter by created date from
         * @param endDate       Filter by created date to
         * @param isDeleted     true for deleted categories, false for active
         * @param sortColumn    SQL column expression to sort by
         * @param sortDirection Sort direction: "ASC" or "DESC"
         * @param offset        Pagination offset
         * @param limit         Pagination limit
         * @return List of CategoryResponse
         */
        List<CategoryResponse> findAllWithFilterAndSearch(
                        @Param("keyword") String keyword,
                        @Param("parentId") Long parentId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("isDeleted") boolean isDeleted,
                        @Param("sortColumn") String sortColumn,
                        @Param("sortDirection") String sortDirection,
                        @Param("offset") long offset,
                        @Param("limit") int limit);

        /**
         * Count total categories matching the filter criteria.
         *
         * @param keyword   Search keyword
         * @param parentId  Filter by parent category id
         * @param startDate Filter by created date from
         * @param endDate   Filter by created date to
         * @param isDeleted true for deleted categories, false for active
         * @return Total count
         */
        long countWithFilterAndSearch(
                        @Param("keyword") String keyword,
                        @Param("parentId") Long parentId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("isDeleted") boolean isDeleted);

}
