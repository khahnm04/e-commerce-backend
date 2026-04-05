package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.request.UserCreationRequest;
import com.khahnm04.ecommerce.dto.request.UserUpdateRequest;
import com.khahnm04.ecommerce.entity.User;
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
