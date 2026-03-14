package com.khahnm04.ecommerce.modules.category.mapper;

import com.khahnm04.ecommerce.modules.category.dto.request.CategoryRequest;
import com.khahnm04.ecommerce.modules.category.dto.response.CategoryResponse;
import com.khahnm04.ecommerce.modules.category.entity.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "parent", ignore = true)
    Category toCategory(CategoryRequest request);

    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    CategoryResponse toCategoryResponse(Category category);

    @Mapping(target = "parent", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCategory(@MappingTarget Category category, CategoryRequest request);

}
