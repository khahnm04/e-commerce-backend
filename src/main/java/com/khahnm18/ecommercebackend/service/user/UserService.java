package com.khahnm18.ecommercebackend.service.user;

import com.khahnm18.ecommercebackend.enums.AccountStatus;
import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.user.UserCreationRequest;
import com.khahnm18.ecommercebackend.dto.request.user.UserUpdateRequest;
import com.khahnm18.ecommercebackend.dto.response.user.UserResponse;
import org.springframework.data.domain.Page;
import java.time.Instant;
import java.util.UUID;

public interface UserService {

    UserResponse getUserDetailById(UUID accountId, boolean isDeleted);

    Page<UserResponse> getAllUsers(
        int page,
        int size,
        String sort,
        String keyword,
        boolean isDeleted,
        Instant startDate,
        Instant endDate,
        AccountStatus status
    );

    UserResponse createUser(UserCreationRequest request);

    UserResponse updateUser(UUID accountId, UserUpdateRequest request);

    void softDeleteUser(UUID accountId);

    void restoreUser(UUID accountId);

    void bulkAction(BulkActionRequest request);

    void deleteUser(UUID accountId);

}
