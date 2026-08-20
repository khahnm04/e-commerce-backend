package com.khahnm04.ecommerce.mapper.user;

import com.khahnm04.ecommerce.constant.user.AccountAdminStatus;
import com.khahnm04.ecommerce.constant.user.Gender;
import com.khahnm04.ecommerce.dto.projection.AccountAdminProjection;
import com.khahnm04.ecommerce.dto.request.user.AccountAdminCreationRequest;
import com.khahnm04.ecommerce.dto.request.user.AccountAdminUpdateRequest;
import com.khahnm04.ecommerce.dto.request.user.AdminProfileRequest;
import com.khahnm04.ecommerce.dto.response.role.RoleResponse;
import com.khahnm04.ecommerce.dto.response.user.AccountAdminResponse;
import com.khahnm04.ecommerce.dto.response.user.AdminProfileResponse;
import com.khahnm04.ecommerce.entity.user.AccountAdmin;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AccountAdminMapper {

    private final PasswordEncoder passwordEncoder;

    public AccountAdmin toAccountAdmin(AccountAdminCreationRequest request) {
        return AccountAdmin.builder()
            .fullName(request.fullName())
            .jobTitle(request.jobTitle())
            .email(request.email())
            .phoneNumber(request.phoneNumber())
            .password(passwordEncoder.encode(request.password()))
            .dateOfBirth(request.dateOfBirth())
            .avatar(request.avatar())
            .gender(request.gender() == null ? Gender.OTHER : request.gender())
            .status(request.status() == null ? AccountAdminStatus.ACTIVE : request.status())
            .build();
    }

    public AccountAdminResponse toAccountAdminResponse(AccountAdmin accountAdmin, Set<RoleResponse> roles) {
        return toAccountAdminResponse(accountAdmin, roles, null);
    }

    public AccountAdminResponse toAccountAdminResponse(AccountAdmin accountAdmin, Set<RoleResponse> roles, List<String> permissions) {
        AccountAdminResponse.AccountAdminResponseBuilder builder = AccountAdminResponse.builder()
            .id(accountAdmin.getId())
            .fullName(accountAdmin.getFullName())
            .jobTitle(accountAdmin.getJobTitle())
            .email(accountAdmin.getEmail())
            .phoneNumber(accountAdmin.getPhoneNumber())
            .dateOfBirth(accountAdmin.getDateOfBirth())
            .avatar(accountAdmin.getAvatar())
            .gender(accountAdmin.getGender())
            .status(accountAdmin.getStatus())
            .lastLoginAt(accountAdmin.getLastLoginAt())
            .createdAt(accountAdmin.getCreatedAt())
            .createdBy(accountAdmin.getCreatedBy())
            .updatedAt(accountAdmin.getUpdatedAt())
            .updatedBy(accountAdmin.getUpdatedBy())
            .deletedAt(accountAdmin.getDeletedAt())
            .deletedBy(accountAdmin.getDeletedBy());
        if (roles != null) {
            builder.roles(roles);
        }
        if (permissions != null) {
            builder.permissions(permissions);
        }
        return builder.build();
    }

    public AccountAdminResponse toAccountAdminResponse(AccountAdminProjection projection) {
        return AccountAdminResponse.builder()
            .id(projection.getId())
            .fullName(projection.getFullName())
            .jobTitle(projection.getJobTitle())
            .email(projection.getEmail())
            .phoneNumber(projection.getPhoneNumber())
            .dateOfBirth(projection.getDateOfBirth())
            .avatar(projection.getAvatar())
            .gender(projection.getGender())
            .status(projection.getStatus())
            .lastLoginAt(projection.getLastLoginAt())
            .createdAt(projection.getCreatedAt())
            .createdBy(projection.getCreatedBy())
            .updatedAt(projection.getUpdatedAt())
            .updatedBy(projection.getUpdatedBy())
            .deletedAt(projection.getDeletedAt())
            .deletedBy(projection.getDeletedBy())
            .build();
    }

    public AdminProfileResponse toAdminProfileResponse(AccountAdmin accountAdmin) {
        return AdminProfileResponse.builder()
            .fullName(accountAdmin.getFullName())
            .jobTitle(accountAdmin.getJobTitle())
            .email(accountAdmin.getEmail())
            .phoneNumber(accountAdmin.getPhoneNumber())
            .dateOfBirth(accountAdmin.getDateOfBirth())
            .avatar(accountAdmin.getAvatar())
            .gender(accountAdmin.getGender())
            .build();
    }

    public void updateAccountAdmin(AccountAdmin accountAdmin, AccountAdminUpdateRequest request) {
        if (request.fullName() != null) {
            accountAdmin.setFullName(request.fullName());
        }
        if (request.jobTitle() != null) {
            accountAdmin.setJobTitle(request.jobTitle());
        }
        if (request.email() != null) {
            accountAdmin.setEmail(request.email());
        }
        if (request.phoneNumber() != null) {
            accountAdmin.setPhoneNumber(request.phoneNumber());
        }
        if (request.dateOfBirth() != null) {
            accountAdmin.setDateOfBirth(request.dateOfBirth());
        }
        if (request.avatar() != null) {
            accountAdmin.setAvatar(request.avatar());
        }
        if (request.gender() != null) {
            accountAdmin.setGender(request.gender());
        }
        if (request.status() != null) {
            accountAdmin.setStatus(request.status());
        }
    }

    public void updateAdminProfile(AccountAdmin accountAdmin, AdminProfileRequest request) {
        if (request.fullName() != null) {
            accountAdmin.setFullName(request.fullName());
        }
        if (request.email() != null) {
            accountAdmin.setEmail(request.email());
        }
        if (request.phoneNumber() != null) {
            accountAdmin.setPhoneNumber(request.phoneNumber());
        }
        if (request.dateOfBirth() != null) {
            accountAdmin.setDateOfBirth(request.dateOfBirth());
        }
        if (request.avatar() != null) {
            accountAdmin.setAvatar(request.avatar());
        }
        if (request.gender() != null) {
            accountAdmin.setGender(request.gender());
        }
    }

}
