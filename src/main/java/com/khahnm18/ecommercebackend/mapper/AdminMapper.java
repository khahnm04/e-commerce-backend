package com.khahnm18.ecommercebackend.mapper;

import com.khahnm18.ecommercebackend.dto.request.user.AdminCreationRequest;
import com.khahnm18.ecommercebackend.dto.request.user.AdminUpdateRequest;
import com.khahnm18.ecommercebackend.dto.request.user.MyProfileAdminRequest;
import com.khahnm18.ecommercebackend.dto.response.auth.RoleResponse;
import com.khahnm18.ecommercebackend.dto.response.user.AdminResponse;
import com.khahnm18.ecommercebackend.dto.response.user.MyProfileAdminResponse;
import com.khahnm18.ecommercebackend.entity.auth.Account;
import com.khahnm18.ecommercebackend.entity.auth.Role;
import com.khahnm18.ecommercebackend.entity.user.AdminProfile;
import org.mapstruct.*;
import java.util.List;
import java.util.UUID;

@Mapper(
    componentModel = "spring",
    uses = {RoleMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public interface AdminMapper {

    @Mapping(target = "gender", defaultValue = "OTHER")
    AdminProfile toAdminProfile(AdminCreationRequest request, UUID accountId);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    AdminResponse toAdminResponse(AdminProfile adminProfile, Account account);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    AdminResponse toAdminResponse(AdminProfile adminProfile, Account account, List<Role> roles);

    @AfterMapping
    default void fillAdminResponse(
        AdminProfile adminProfile,
        Account account,
        @MappingTarget AdminResponse.AdminResponseBuilder builder
    ) {
        if (adminProfile != null) {
            builder.id(adminProfile.getId());
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

    List<RoleResponse> mapRoles(List<Role> roles);

    MyProfileAdminResponse toAdminProfileResponse(AdminProfile adminProfile, Account account);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAdmin(@MappingTarget AdminProfile adminProfile, AdminUpdateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAdminProfile(@MappingTarget AdminProfile adminProfile, MyProfileAdminRequest request);

}
