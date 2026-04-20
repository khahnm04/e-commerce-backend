package com.khahnm04.ecommerce.mapper;

import com.khahnm04.ecommerce.dto.request.NewsRequest;
import com.khahnm04.ecommerce.dto.response.NewsResponse;
import com.khahnm04.ecommerce.entity.News;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsMapper {

    News toNews(NewsRequest request);

    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    @Mapping(target = "deletedBy", source = "deletedBy.fullName")
    NewsResponse toNewsResponse(News news);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateNews(@MappingTarget News news, NewsRequest request);

}
