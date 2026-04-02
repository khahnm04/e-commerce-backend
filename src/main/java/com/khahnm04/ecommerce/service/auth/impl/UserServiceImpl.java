package com.khahnm04.ecommerce.service.auth.impl;

import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.dto.request.auth.UserCreationRequest;
import com.khahnm04.ecommerce.dto.request.auth.UserUpdateRequest;
import com.khahnm04.ecommerce.entity.auth.Role;
import com.khahnm04.ecommerce.entity.auth.User;
import com.khahnm04.ecommerce.common.enums.Gender;
import com.khahnm04.ecommerce.common.enums.UserStatus;
import com.khahnm04.ecommerce.mapper.auth.UserMapper;
import com.khahnm04.ecommerce.repository.auth.RoleRepository;
import com.khahnm04.ecommerce.repository.auth.UserRepository;
import com.khahnm04.ecommerce.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User createUser(UserCreationRequest request) {
        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new AppException(ErrorCode.PHONE_NUMBER_EXISTED);
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        User newUser = userMapper.toUser(request);
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setStatus(request.status() == null ? UserStatus.ACTIVE : request.status());
        newUser.setGender(request.gender() == null ? Gender.OTHER : request.gender());
        assignRoleToUser(newUser, request.roles());

        User savedUser = userRepository.save(newUser);
        log.info("Created new user with id: {}", savedUser.getId());
        return savedUser;
    }

    @Override
    @Transactional
    public void updateUser(Long id, UserUpdateRequest request) {
        if (StringUtils.hasText(request.phoneNumber())
                && userRepository.existsByPhoneNumberAndIdNot(request.phoneNumber(), id)) {
            throw new AppException(ErrorCode.PHONE_NUMBER_EXISTED);
        }
        if (StringUtils.hasText(request.email()) &&
                userRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        User user = getUserById(id);
        userMapper.updateUser(user, request);

        if (request.roles() != null) {
            assignRoleToUser(user, request.roles());
        }
        userRepository.save(user);
    }

    @Override
    public Map<Long, Set<String>> getRoleNamesByUserIds(Set<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return userRepository.findAllByIdInWithRoles(userIds)
                .stream()
                .collect(Collectors.toMap(
                        User::getId,
                        user -> user.getRoles() != null
                                ? user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
                                : Set.of()));
    }

    @Override
    @Transactional
    public void updateUserStatus(Long id, UserStatus status) {
        User user = getUserById(id);
        if (user.getStatus() == status) {
            return;
        }
        user.setStatus(status);
        userRepository.save(user);
        log.info("Updated status for user {} to {}", id, status);
    }

    @Override
    @Transactional
    public void bulkUpdateStatus(Set<Long> userIds, UserStatus status) {
        userRepository.updateStatusByIds(status, userIds);
        log.info("Bulk updated status to {} for {} users", status, userIds.size());
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private void assignRoleToUser(User user, Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            user.setRoles(Collections.emptySet());
            return;
        }

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));

        if (roles.size() != roleIds.size()) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
        user.setRoles(roles);
    }

}
