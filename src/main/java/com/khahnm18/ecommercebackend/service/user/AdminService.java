package com.khahnm18.ecommercebackend.service.user;

import com.khahnm18.ecommercebackend.enums.AccountStatus;
import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.user.*;
import com.khahnm18.ecommercebackend.dto.response.user.MyProfileAdminResponse;
import com.khahnm18.ecommercebackend.dto.response.user.AdminResponse;
import org.springframework.data.domain.Page;
import java.time.Instant;
import java.util.UUID;

public interface AdminService {

    AdminResponse getAdminDetailById(UUID accountId, boolean isDeleted);

    Page<AdminResponse> getAllAdmins(
        int page,
        int size,
        String keyword,
        boolean isDeleted,
        Instant startDate,
        Instant endDate,
        AccountStatus status
    );

    MyProfileAdminResponse getMyProfile();

    AdminResponse createAdmin(AdminCreationRequest request);

    AdminResponse updateAdmin(UUID accountId, AdminUpdateRequest request);

    MyProfileAdminResponse updateMyProfileAdmin(MyProfileAdminRequest request);

    void changePassword(ChangePasswordRequest request);

    void softDeleteAdmin(UUID accountId);

    void restoreAdmin(UUID accountId);

    void bulkAction(BulkActionRequest request);

    void resetPassword(UUID accountId, ResetPasswordRequest request);

    void deleteAdmin(UUID accountId);

}
