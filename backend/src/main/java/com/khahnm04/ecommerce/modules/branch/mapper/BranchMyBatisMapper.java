package com.khahnm04.ecommerce.modules.branch.mapper;

import com.khahnm04.ecommerce.modules.branch.dto.response.BranchResponse;
import com.khahnm04.ecommerce.modules.branch.enums.BranchStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface BranchMyBatisMapper {

    /**
     * Search and filter branches with pagination.
     *
     * @param keyword       Search keyword (matches code, name, phoneNumber, email,
     *                      addressDetail, city)
     * @param status        Filter by branch status
     * @param startDate     Filter by created date from
     * @param endDate       Filter by created date to
     * @param isDeleted     true for deleted branches, false for active
     * @param sortColumn    SQL column expression to sort by
     * @param sortDirection Sort direction: "ASC" or "DESC"
     * @param offset        Pagination offset
     * @param limit         Pagination limit
     * @return List of BranchResponse
     */
    List<BranchResponse> findAllWithFilterAndSearch(
            @Param("keyword") String keyword,
            @Param("status") BranchStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("isDeleted") boolean isDeleted,
            @Param("sortColumn") String sortColumn,
            @Param("sortDirection") String sortDirection,
            @Param("offset") long offset,
            @Param("limit") int limit
    );

    /**
     * Count total branches matching the filter criteria.
     *
     * @param keyword   Search keyword
     * @param status    Filter by branch status
     * @param startDate Filter by created date from
     * @param endDate   Filter by created date to
     * @param isDeleted true for deleted branches, false for active
     * @return Total count
     */
    long countWithFilterAndSearch(
            @Param("keyword") String keyword,
            @Param("status") BranchStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("isDeleted") boolean isDeleted
    );

}
