package com.khahnm04.ecommerce.modules.auth.mapper.mybaits;

import com.khahnm04.ecommerce.modules.auth.dto.response.RoleResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RoleMyBatisMapper {

    List<RoleResponse> findAllWithFilterAndSearch(
            @Param("keyword") String keyword,
            @Param("isDeleted") boolean isDeleted,
            @Param("sortColumn") String sortColumn,
            @Param("sortDirection") String sortDirection,
            @Param("offset") long offset,
            @Param("limit") int limit);

    long countWithFilterAndSearch(
            @Param("keyword") String keyword,
            @Param("isDeleted") boolean isDeleted);

}
