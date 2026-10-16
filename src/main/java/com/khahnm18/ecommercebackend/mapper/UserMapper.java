package com.khahnm18.ecommercebackend.mapper;

import com.khahnm18.ecommercebackend.dto.request.auth.RegisterRequest;
import com.khahnm18.ecommercebackend.dto.request.user.UserCreationRequest;
import com.khahnm18.ecommercebackend.dto.request.user.UserUpdateRequest;
import com.khahnm18.ecommercebackend.dto.response.user.MyProfileUserResponse;
import com.khahnm18.ecommercebackend.dto.response.user.UserResponse;
import com.khahnm18.ecommercebackend.entity.auth.Account;
import com.khahnm18.ecommercebackend.entity.user.UserProfile;
import org.mapstruct.*;
import java.util.UUID;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "gender", defaultValue = "OTHER")
    UserProfile toUserProfile(UserCreationRequest request, UUID accountId);

    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "dateOfBirth", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    UserProfile toUserProfile(RegisterRequest request, UUID accountId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    UserResponse toUserResponse(UserProfile userProfile, Account account);

    @AfterMapping
    default void fillUserResponse(
        UserProfile userProfile,
        Account account,
        @MappingTarget UserResponse.UserResponseBuilder builder
    ) {
        if (userProfile != null) {
            builder.id(userProfile.getId());
        }
        if (account != null) {
            builder.accountId(account.getId())
                .createdAt(account.getCreatedAt())
                .createdBy(account.getCreatedBy())
                .updatedAt(account.getUpdatedAt())
                .updatedBy(account.getUpdatedBy())
                .deletedAt(account.getDeletedAt())
                .deletedBy(account.getDeletedBy())
                .isDeleted(account.getIsDeleted());
        }
    }

    MyProfileUserResponse toUserProfileResponse(UserProfile userProfile, Account account);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserProfile(@MappingTarget UserProfile userProfile, UserUpdateRequest request);

}
