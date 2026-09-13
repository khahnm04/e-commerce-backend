package com.khahnm18.ecommercebackend.mapper.user;

import com.khahnm18.ecommercebackend.dto.request.user.AdminCreationRequest;
import com.khahnm18.ecommercebackend.entity.user.AdminProfile;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import java.util.Collections;
import java.util.Objects;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdminProfileMapper {

    public AdminProfile toAdminProfile(AdminCreationRequest request, UUID accountId) {
        if (request == null) return null;
        return AdminProfile.builder()
            .accountId(accountId)
            .jobTitle(request.jobTitle())
            .fullName(request.fullName())
            .dateOfBirth(request.dateOfBirth())
            .avatar(request.avatar())
            .gender(request.gender())
            .build();
    }

}
