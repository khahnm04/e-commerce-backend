package com.khahnm04.ecommerce.modules.auth.mapper;

import com.khahnm04.ecommerce.modules.auth.dto.request.RoleRequest;
import com.khahnm04.ecommerce.modules.auth.dto.response.RoleResponse;
import com.khahnm04.ecommerce.modules.auth.entity.Permission;
import com.khahnm04.ecommerce.modules.auth.entity.Role;
import org.mapstruct.*;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    @Mapping(target = "permissions", expression = "java(mapPermissionsToCode(role.getPermissions()))")
    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    RoleResponse toRoleResponse(Role role);

    @Mapping(target = "permissions", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRole(@MappingTarget Role role, RoleRequest request);

    default Set<String> mapPermissionsToCode(Set<Permission> permissions) {
        if (permissions == null) {
            return null;
        }
        return permissions.stream()
                .map(Permission::getCode)
                .collect(Collectors.toSet());
    }

}
