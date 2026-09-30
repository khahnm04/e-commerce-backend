package com.khahnm18.ecommercebackend.mapper.user;

import com.khahnm18.ecommercebackend.dto.request.user.UserCreationRequest;
import com.khahnm18.ecommercebackend.dto.request.user.UserUpdateRequest;
import com.khahnm18.ecommercebackend.dto.response.user.UserResponse;
import com.khahnm18.ecommercebackend.entity.auth.Account;
import com.khahnm18.ecommercebackend.entity.user.UserProfile;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class UserMapper {

    public UserProfile toUserProfile(UserCreationRequest request, UUID accountId) {
        if (request == null) return null;
        return UserProfile.builder()
            .accountId(accountId)
            .fullName(request.fullName())
            .dateOfBirth(request.dateOfBirth())
            .avatar(request.avatar())
            .gender(request.gender())
            .build();
    }

    public UserResponse toUserResponse(UserProfile userProfile, Account account) {
        if (account == null) return null;
        return UserResponse.builder()
            .id(userProfile != null ? userProfile.getId() : null)
            .accountId(account.getId())
            .email(account.getEmail())
            .phoneNumber(account.getPhoneNumber())
            .fullName(userProfile != null ? userProfile.getFullName() : null)
            .dateOfBirth(userProfile != null ? userProfile.getDateOfBirth() : null)
            .avatar(userProfile != null ? userProfile.getAvatar() : null)
            .gender(userProfile != null ? userProfile.getGender() : null)
            .totalPoint(userProfile != null ? userProfile.getTotalPoint() : null)
            .usedPoint(userProfile != null ? userProfile.getUsedPoint() : null)
            .status(account.getStatus())
            .lastLoginAt(account.getLastLoginAt())
            .createdAt(account.getCreatedAt())
            .createdBy(account.getCreatedBy())
            .updatedAt(account.getUpdatedAt())
            .updatedBy(account.getUpdatedBy())
            .deletedAt(account.getDeletedAt())
            .deletedBy(account.getDeletedBy())
            .isDeleted(account.getIsDeleted())
            .build();
    }

    public void updateUserProfile(UserProfile userProfile, UserUpdateRequest request) {
        if (request.fullName() != null) {
            userProfile.setFullName(request.fullName());
        }
        if (request.dateOfBirth() != null) {
            userProfile.setDateOfBirth(request.dateOfBirth());
        }
        if (request.avatar() != null) {
            userProfile.setAvatar(request.avatar());
        }
        if (request.gender() != null) {
            userProfile.setGender(request.gender());
        }
    }

}
