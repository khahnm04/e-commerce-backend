package com.khahnm04.ecommerce.mapper.auth;

import com.khahnm04.ecommerce.dto.response.auth.PermissionResponse;
import com.khahnm04.ecommerce.entity.auth.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper {

    PermissionResponse toPermissionResponse(Permission permission);

}
