package com.khahnm04.ecommerce.modules.catalog.mapper;

import com.khahnm04.ecommerce.modules.catalog.dto.request.CategoryRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.CategoryResponse;
import com.khahnm04.ecommerce.modules.catalog.entity.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "path", ignore = true)
    @Mapping(target = "level", ignore = true)
    Category toCategory(CategoryRequest request);

    @Mapping(target = "parentId", expression = "java(getParentId(category))")
    @Mapping(target = "parentName", expression = "java(getParentName(category))")
    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    CategoryResponse toCategoryResponse(Category category);

    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "path", ignore = true)
    @Mapping(target = "level", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCategory(@MappingTarget Category category, CategoryRequest request);

    default Long getParentId(Category category) {
        return category.getParent() != null ? category.getParent().getId() : null;
    }

    default String getParentName(Category category) {
        return category.getParent() != null ? category.getParent().getName() : null;
    }

}
