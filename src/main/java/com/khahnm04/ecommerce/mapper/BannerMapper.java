package com.khahnm04.ecommerce.mapper;

import com.khahnm04.ecommerce.dto.request.BannerRequest;
import com.khahnm04.ecommerce.dto.response.BannerResponse;
import com.khahnm04.ecommerce.entity.Banner;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BannerMapper {

    Banner toBanner(BannerRequest request);

    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    @Mapping(target = "deletedBy", source = "deletedBy.fullName")
    BannerResponse toBannerResponse(Banner banner);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBanner(@MappingTarget Banner banner, BannerRequest request);

}
