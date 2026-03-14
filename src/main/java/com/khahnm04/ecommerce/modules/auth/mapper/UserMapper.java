package com.khahnm04.ecommerce.modules.auth.mapper;

import com.khahnm04.ecommerce.modules.auth.dto.request.UserCreationRequest;
import com.khahnm04.ecommerce.modules.auth.dto.request.UserUpdateRequest;
import com.khahnm04.ecommerce.modules.auth.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    @Mapping(target = "roles", ignore = true)

    @Mapping(target = "isEmailVerified", ignore = true)
    @Mapping(target = "isPhoneVerified", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

}
