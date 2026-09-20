package com.khahnm18.ecommercebackend.mapper.user;

import com.khahnm18.ecommercebackend.dto.request.user.AdminCreationRequest;
import com.khahnm18.ecommercebackend.dto.request.user.AdminUpdateRequest;
import com.khahnm18.ecommercebackend.dto.response.auth.RoleResponse;
import com.khahnm18.ecommercebackend.dto.response.user.AdminProfileResponse;
import com.khahnm18.ecommercebackend.dto.response.user.AdminResponse;
import com.khahnm18.ecommercebackend.entity.auth.Account;
import com.khahnm18.ecommercebackend.entity.auth.Role;
import com.khahnm18.ecommercebackend.entity.user.AdminProfile;
import com.khahnm18.ecommercebackend.mapper.auth.RoleMapper;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import java.util.Collections;
import java.util.Objects;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdminMapper {

    private final RoleMapper roleMapper;

    public AdminProfile toAdminProfile(AdminCreationRequest request, UUID accountId) {
        if (request == null) return null;
        return AdminProfile.builder()
            .accountId(accountId)
            .fullName(request.fullName())
            .jobTitle(request.jobTitle())
            .dateOfBirth(request.dateOfBirth())
            .avatar(request.avatar())
            .gender(request.gender())
            .build();
    }

    public AdminResponse toAdminResponseWithRoles(AdminProfile adminProfile, Account account, List<Role> roles) {
        if (account == null) return null;

        List<RoleResponse> roleResponses = (roles == null)
            ? Collections.emptyList()
            : roles.stream()
                .filter(Objects::nonNull)
                .map(roleMapper::toRoleResponse)
                .toList();

        return AdminResponse.builder()
            .id(adminProfile.getId())
            .accountId(account.getId())
            .email(account.getEmail())
            .phoneNumber(account.getPhoneNumber())
            .jobTitle(adminProfile.getJobTitle())
            .fullName(adminProfile.getFullName())
            .dateOfBirth(adminProfile.getDateOfBirth())
            .avatar(adminProfile.getAvatar())
            .gender(adminProfile.getGender())
            .roles(roleResponses)
            .createdAt(adminProfile.getCreatedAt())
            .createdBy(adminProfile.getCreatedBy())
            .updatedAt(adminProfile.getUpdatedAt())
            .updatedBy(adminProfile.getUpdatedBy())
            .deletedAt(adminProfile.getDeletedAt())
            .deletedBy(adminProfile.getDeletedBy())
            .isDeleted(adminProfile.getIsDeleted())
            .build();
    }

    public AdminResponse toAdminResponse(AdminProfile adminProfile, Account account) {
        if (account == null) return null;
        return AdminResponse.builder()
            .id(adminProfile.getId())
            .accountId(account.getId())
            .email(account.getEmail())
            .phoneNumber(account.getPhoneNumber())
            .jobTitle(adminProfile.getJobTitle())
            .fullName(adminProfile.getFullName())
            .dateOfBirth(adminProfile.getDateOfBirth())
            .avatar(adminProfile.getAvatar())
            .gender(adminProfile.getGender())
            .createdAt(adminProfile.getCreatedAt())
            .createdBy(adminProfile.getCreatedBy())
            .updatedAt(adminProfile.getUpdatedAt())
            .updatedBy(adminProfile.getUpdatedBy())
            .deletedAt(adminProfile.getDeletedAt())
            .deletedBy(adminProfile.getDeletedBy())
            .isDeleted(adminProfile.getIsDeleted())
            .build();
    }

    public void updateAdmin(AdminProfile adminProfile, AdminUpdateRequest request) {
        if (request.jobTitle() != null) {
            adminProfile.setJobTitle(request.jobTitle());
        }
        if (request.fullName() != null) {
            adminProfile.setFullName(request.fullName());
        }
        if (request.dateOfBirth() != null) {
            adminProfile.setDateOfBirth(request.dateOfBirth());
        }
        if (request.avatar() != null) {
            adminProfile.setAvatar(request.avatar());
        }
        if (request.gender() != null) {
            adminProfile.setGender(request.gender());
        }
    }

    public AdminProfileResponse toAdminProfileResponse(AdminProfile adminProfile, Account account) {
        if (account == null) return null;
        return AdminProfileResponse.builder()
            .email(account.getEmail())
            .phoneNumber(account.getPhoneNumber())
            .jobTitle(adminProfile.getJobTitle())
            .fullName(adminProfile.getFullName())
            .dateOfBirth(adminProfile.getDateOfBirth())
            .avatar(adminProfile.getAvatar())
            .gender(adminProfile.getGender())
            .build();
    }

}
