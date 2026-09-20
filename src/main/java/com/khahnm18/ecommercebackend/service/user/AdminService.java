package com.khahnm18.ecommercebackend.service.user;

import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.user.AdminCreationRequest;
import com.khahnm18.ecommercebackend.dto.request.user.AdminUpdateRequest;
import com.khahnm18.ecommercebackend.dto.response.user.AdminProfileResponse;
import com.khahnm18.ecommercebackend.dto.response.user.AdminResponse;
import org.springframework.data.domain.Page;
import java.time.Instant;
import java.util.UUID;

public interface AdminService {

    AdminResponse getAdminDetailById(UUID accountId, boolean isDeleted);

    Page<AdminResponse> getAllAdmins(
        int page,
        int size,
        String sort,
        String keyword,
        boolean isDeleted,
        Instant startDate,
        Instant endDate
    );

    AdminProfileResponse getMyProfile();

    AdminResponse createAdmin(AdminCreationRequest request);

    AdminResponse updateAdmin(UUID accountId, AdminUpdateRequest request);

    void softDeleteAdmin(UUID accountId);

    void restoreAdmin(UUID accountId);

    void bulkAction(BulkActionRequest request);

    void deleteAdmin(UUID accountId);

}
