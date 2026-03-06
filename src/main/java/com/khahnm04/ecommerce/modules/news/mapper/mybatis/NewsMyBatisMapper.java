package com.khahnm04.ecommerce.modules.news.mapper.mybatis;

import com.khahnm04.ecommerce.modules.news.dto.response.NewsProductResponse;
import com.khahnm04.ecommerce.modules.news.dto.response.NewsResponse;
import com.khahnm04.ecommerce.modules.news.enums.NewsStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface NewsMyBatisMapper {

        List<NewsResponse> findAllWithFilterAndSearch(
                        @Param("status") NewsStatus status,
                        @Param("keyword") String keyword,
                        @Param("isFeatured") Boolean isFeatured,
                        @Param("publishedOnly") boolean publishedOnly,
                        @Param("deletedOnly") boolean deletedOnly,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("sortColumn") String sortColumn,
                        @Param("sortDirection") String sortDirection,
                        @Param("offset") long offset,
                        @Param("limit") int limit);

        long countWithFilterAndSearch(
                        @Param("status") NewsStatus status,
                        @Param("keyword") String keyword,
                        @Param("isFeatured") Boolean isFeatured,
                        @Param("publishedOnly") boolean publishedOnly,
                        @Param("deletedOnly") boolean deletedOnly,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        List<NewsProductResponse> findProductsByNewsId(@Param("newsId") Long newsId);

}
