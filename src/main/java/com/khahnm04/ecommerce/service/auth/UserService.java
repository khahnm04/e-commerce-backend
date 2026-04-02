package com.khahnm04.ecommerce.service.auth;

import com.khahnm04.ecommerce.dto.request.auth.UserCreationRequest;
import com.khahnm04.ecommerce.dto.request.auth.UserUpdateRequest;
import com.khahnm04.ecommerce.entity.auth.User;
import com.khahnm04.ecommerce.common.enums.UserStatus;
import java.util.Map;
import java.util.Set;

public interface UserService {

    User createUser(UserCreationRequest request);

    void updateUser(Long id, UserUpdateRequest request);

    Map<Long, Set<String>> getRoleNamesByUserIds(Set<Long> userIds);

    void updateUserStatus(Long userId, UserStatus status);

    void bulkUpdateStatus(Set<Long> userIds, UserStatus status);

}
