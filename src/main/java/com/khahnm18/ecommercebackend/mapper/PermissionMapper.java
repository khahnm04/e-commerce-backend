package com.khahnm18.ecommercebackend.mapper;

import com.khahnm18.ecommercebackend.dto.response.auth.PermissionResponse;
import com.khahnm18.ecommercebackend.entity.auth.Permission;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PermissionMapper {

    PermissionResponse toPermissionResponse(Permission permission);

}
