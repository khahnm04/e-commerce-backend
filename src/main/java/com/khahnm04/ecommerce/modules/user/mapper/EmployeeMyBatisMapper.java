package com.khahnm04.ecommerce.modules.user.mapper;

import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import com.khahnm04.ecommerce.modules.user.dto.response.EmployeeResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface EmployeeMyBatisMapper {

        /**
         * Search and filter employees with pagination.
         * 
         * @param keyword       Search keyword (matches fullName, code, email, phoneNumber)
         * @param branchId      Filter by branch ID
         * @param status        Filter by user status
         * @param startDate     Filter by created date from
         * @param endDate       Filter by created date to
         * @param isDeleted     true for deleted employees, false for active
         * @param sortColumn    SQL column expression to sort by
         * @param sortDirection Sort direction: "ASC" or "DESC"
         * @param offset        Pagination offset
         * @param limit         Pagination limit
         * @return List of EmployeeResponse
         */
        List<EmployeeResponse> findAllWithFilterAndSearch(
                        @Param("keyword") String keyword,
                        @Param("branchId") Long branchId,
                        @Param("status") UserStatus status,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("isDeleted") boolean isDeleted,
                        @Param("sortColumn") String sortColumn,
                        @Param("sortDirection") String sortDirection,
                        @Param("offset") long offset,
                        @Param("limit") int limit
        );

        /**
         * Count total employees matching the filter criteria.
         * 
         * @param keyword   Search keyword
         * @param branchId  Filter by branch ID
         * @param status    Filter by user status
         * @param startDate Filter by created date from
         * @param endDate   Filter by created date to
         * @param isDeleted true for deleted employees, false for active
         * @return Total count
         */
        long countWithFilterAndSearch(
                        @Param("keyword") String keyword,
                        @Param("branchId") Long branchId,
                        @Param("status") UserStatus status,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("isDeleted") boolean isDeleted
        );

}
