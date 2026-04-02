package com.khahnm04.ecommerce.mapper.auth;

import com.khahnm04.ecommerce.dto.request.auth.UserCreationRequest;
import com.khahnm04.ecommerce.dto.request.auth.UserUpdateRequest;
import com.khahnm04.ecommerce.entity.auth.User;
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
