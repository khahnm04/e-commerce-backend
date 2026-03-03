package com.khahnm04.ecommerce.modules.auth.service;

import com.khahnm04.ecommerce.modules.auth.dto.request.UserCreationRequest;
import com.khahnm04.ecommerce.modules.auth.dto.request.UserUpdateRequest;
import com.khahnm04.ecommerce.modules.auth.entity.User;
import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import java.util.Map;
import java.util.Set;

public interface UserService {

    User createUser(UserCreationRequest request);

    void updateUser(Long id, UserUpdateRequest request);

    Map<Long, Set<String>> getRoleNamesByUserIds(Set<Long> userIds);

    void updateUserStatus(Long userId, UserStatus status);

    void softDeleteUser(Long userId);

    void restoreUser(Long userId);

    void bulkUpdateStatus(Set<Long> userIds, UserStatus status);

}
