package com.khahnm04.ecommerce.modules.user.mapper;

import com.khahnm04.ecommerce.modules.auth.enums.Gender;
import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import com.khahnm04.ecommerce.modules.user.dto.response.CustomerResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface CustomerMyBatisMapper {

        /**
         * Search and filter customers with pagination.
         *
         * @param keyword       Search keyword (matches fullName, email, phoneNumber)
         * @param status        Filter by user status
         * @param gender        Filter by gender
         * @param startDate     Filter by created date from
         * @param endDate       Filter by created date to
         * @param isDeleted     true for deleted customers, false for active
         * @param sortColumn    SQL column expression to sort by
         * @param sortDirection Sort direction: "ASC" or "DESC"
         * @param offset        Pagination offset
         * @param limit         Pagination limit
         * @return List of CustomerResponse
         */
        List<CustomerResponse> findAllWithFilterAndSearch(
                        @Param("keyword") String keyword,
                        @Param("status") UserStatus status,
                        @Param("gender") Gender gender,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("isDeleted") boolean isDeleted,
                        @Param("sortColumn") String sortColumn,
                        @Param("sortDirection") String sortDirection,
                        @Param("offset") long offset,
                        @Param("limit") int limit);

        /**
         * Count total customers matching the filter criteria.
         *
         * @param keyword   Search keyword
         * @param status    Filter by user status
         * @param gender    Filter by gender
         * @param startDate Filter by created date from
         * @param endDate   Filter by created date to
         * @param isDeleted true for deleted customers, false for active
         * @return Total count
         */
        long countWithFilterAndSearch(
                        @Param("keyword") String keyword,
                        @Param("status") UserStatus status,
                        @Param("gender") Gender gender,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("isDeleted") boolean isDeleted);

}
