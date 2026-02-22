package com.khahnm04.ecommerce.modules.auth.service.impl;

import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.modules.auth.dto.request.UserCreationRequest;
import com.khahnm04.ecommerce.modules.auth.dto.request.UserUpdateRequest;
import com.khahnm04.ecommerce.modules.auth.entity.Role;
import com.khahnm04.ecommerce.modules.auth.entity.User;
import com.khahnm04.ecommerce.modules.auth.enums.Gender;
import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import com.khahnm04.ecommerce.modules.auth.mapper.UserMapper;
import com.khahnm04.ecommerce.modules.auth.repository.RoleRepository;
import com.khahnm04.ecommerce.modules.auth.repository.UserRepository;
import com.khahnm04.ecommerce.modules.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
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
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new AppException(ErrorCode.PHONE_NUMBER_EXISTED);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        User newUser = userMapper.toUser(request);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setStatus(request.getStatus() == null ? UserStatus.ACTIVE : request.getStatus());
        newUser.setGender(request.getGender() == null ? Gender.OTHER : request.getGender());
        assignRoleToUser(newUser, request.getRoles());

        User savedUser = userRepository.save(newUser);
        log.info("Created new user with id: {}", savedUser.getId());
        return savedUser;
    }

    @Override
    @Transactional
    public void updateUser(Long id, UserUpdateRequest request) {
        if (StringUtils.hasText(request.getPhoneNumber())
                && userRepository.existsByPhoneNumberAndIdNot(request.getPhoneNumber(), id)) {
            throw new AppException(ErrorCode.PHONE_NUMBER_EXISTED);
        }
        if (StringUtils.hasText(request.getEmail()) &&
                userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        User user = getUserById(id);
        userMapper.updateUser(user, request);

        if (request.getRoles() != null) {
            assignRoleToUser(user, request.getRoles());
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
    public void softDeleteUser(Long id) {
        User user = getUserById(id);
        user.setDeletedAt(LocalDateTime.now());
        user.setStatus(UserStatus.BANNED);
        userRepository.save(user);
        log.info("Soft deleted user with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreUser(Long id) {
        User user = getUserById(id);

        if (user.getDeletedAt() != null) {
            user.setDeletedAt(null);
            userRepository.save(user);
            log.info("Restored user with id: {}", id);
        }
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
