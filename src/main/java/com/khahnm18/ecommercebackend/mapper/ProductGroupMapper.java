package com.khahnm18.ecommercebackend.mapper;

import com.khahnm18.ecommercebackend.dto.request.product.ProductGroupRequest;
import com.khahnm18.ecommercebackend.dto.response.product.CategoryResponse;
import com.khahnm18.ecommercebackend.dto.response.product.ProductGroupResponse;
import com.khahnm18.ecommercebackend.entity.product.Category;
import com.khahnm18.ecommercebackend.entity.product.ProductGroup;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
    collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE
)
public interface ProductGroupMapper {

    ProductGroup toProductGroup(ProductGroupRequest request);

    @Mapping(target = "categories", ignore = true)
    ProductGroupResponse toProductGroupResponse(ProductGroup productGroup);

    ProductGroupResponse toProductGroupResponse(ProductGroup productGroup, List<Category> categories);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "slug", source = "slug")
    CategoryResponse toCategoryResponse(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductGroup(@MappingTarget ProductGroup productGroup, ProductGroupRequest request);

}
