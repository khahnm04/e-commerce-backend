package com.khahnm18.ecommercebackend.service.user.impl;

import com.khahnm18.ecommercebackend.dto.request.user.UserSearchRequest;
import com.khahnm18.ecommercebackend.entity.BaseEntity;
import com.khahnm18.ecommercebackend.enums.AccountStatus;
import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.user.UserCreationRequest;
import com.khahnm18.ecommercebackend.dto.request.user.UserUpdateRequest;
import com.khahnm18.ecommercebackend.dto.response.user.UserResponse;
import com.khahnm18.ecommercebackend.dto.response.user.MyProfileUserResponse;
import com.khahnm18.ecommercebackend.entity.auth.Account;
import com.khahnm18.ecommercebackend.entity.auth.AccountRole;
import com.khahnm18.ecommercebackend.entity.auth.Role;
import com.khahnm18.ecommercebackend.entity.user.UserProfile;
import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.mapper.AccountMapper;
import com.khahnm18.ecommercebackend.mapper.UserMapper;
import com.khahnm18.ecommercebackend.repository.auth.AccountRepository;
import com.khahnm18.ecommercebackend.repository.auth.AccountRoleRepository;
import com.khahnm18.ecommercebackend.repository.auth.RoleRepository;
import com.khahnm18.ecommercebackend.repository.user.UserProfileRepository;
import com.khahnm18.ecommercebackend.service.user.UserService;
import com.khahnm18.ecommercebackend.util.SecurityUtil;
import com.khahnm18.ecommercebackend.entity.auth.QAccount;
import com.khahnm18.ecommercebackend.entity.user.QUserProfile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j(topic = "USER-SERVICE")
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JPAQueryFactory queryFactory;
    private final AccountMapper accountMapper;
    private final UserMapper userMapper;
    private final AccountRepository accountRepository;
    private final UserProfileRepository userProfileRepository;
    private final RoleRepository roleRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getUserDetailById(UUID accountId, boolean isDeleted) {
        Account account = isDeleted
            ? this.getSoftDeletedAccountById(accountId)
            : this.getAccountById(accountId);

        UserProfile userProfile = getUserProfileByAccountId(accountId);
        return userMapper.toUserResponse(userProfile, account);
    }

    @Override
    public Page<UserResponse> getAllUsers(int page, int size, UserSearchRequest request) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size);
        return userProfileRepository
            .search(request, pageable)
            .map(item -> userMapper.toUserResponse(item.profile(), item.account()));
    }

    @Override
    public MyProfileUserResponse getMyProfile() {
        UUID accountId = SecurityUtil.getCurrentAccount();
        Account account = this.getAccountById(accountId);
        UserProfile userProfile = this.getUserProfileByAccountId(account.getId());
        return userMapper.toUserProfileResponse(userProfile, account);
    }

    @Override
    @Transactional
    public UserResponse createUser(UserCreationRequest request) {
        Account account = accountMapper.toAccount(request);
        account.setPassword(passwordEncoder.encode(request.password()));
        account = this.saveAccount(account);

        UserProfile userProfile = userMapper.toUserProfile(request, account.getId());
        userProfile = userProfileRepository.save(userProfile);

        Role customerRole = roleRepository.findByCodeAndIsDeletedFalse("CUSTOMER")
            .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        accountRoleRepository.save(AccountRole.builder()
            .accountId(account.getId())
            .roleId(customerRole.getId())
            .build());

        log.info("User profile created: fullName={}, email={}", userProfile.getFullName(), account.getEmail());
        return userMapper.toUserResponse(userProfile, account);
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID accountId, UserUpdateRequest request) {
        Account account = getAccountById(accountId);
        UserProfile userProfile = getUserProfileByAccountId(accountId);

        accountMapper.updateAccount(account, request);
        account = this.saveAccount(account);

        userMapper.updateUserProfile(userProfile, request);
        userProfile = userProfileRepository.save(userProfile);

        log.info("User profile updated: accountId={}, email={}", accountId, account.getEmail());
        return userMapper.toUserResponse(userProfile, account);
    }

    @Override
    @Transactional
    public void softDeleteUser(UUID accountId) {
        Account account = getAccountById(accountId);
        this.softDelete(account);
        log.info("User soft deleted with account_id: {}", accountId);
    }

    @Override
    @Transactional
    public void restoreUser(UUID accountId) {
        Account account = getSoftDeletedAccountById(accountId);
        this.restore(account);
        log.info("User restored with account_id: {}", accountId);
    }

    @Override
    @Transactional
    public void bulkAction(BulkActionRequest request) {
        Set<UUID> accountIds = request.ids();
        String action = request.action().toUpperCase();
        List<Account> accounts = accountRepository.findAllById(accountIds);
        switch (action) {
            case "ACTIVE", "INACTIVE", "BANNED", "LOCKED" -> {
                SecurityUtil.requireAuthority("USER_UPDATE");
                accounts.forEach(account -> account.setStatus(AccountStatus.valueOf(action)));
                log.info("Bulk update status {} user with accountIds: {}", action, accountIds);
            }
            case "SOFT_DELETE" -> {
                SecurityUtil.requireAuthority("USER_TRASH");
                accounts.forEach(this::softDelete);
                log.info("Bulk action soft delete user with accountIds: {}", accountIds);
            }
            case "RESTORE" -> {
                SecurityUtil.requireAuthority("USER_TRASH");
                accounts.forEach(this::restore);
                log.info("Bulk action restore user with accountIds: {}", accountIds);
            }
            case "DELETE" -> {
                SecurityUtil.requireAuthority("USER_DELETE");
                accountRepository.deleteAllInBatch(
                    accounts.stream().filter(BaseEntity::getIsDeleted).toList()
                );
                log.info("Bulk action delete user with accountIds: {}", accountIds);
            }
        }
    }

    @Override
    @Transactional
    public void deleteUser(UUID accountId) {
        Account account = getSoftDeletedAccountById(accountId);
        accountRepository.delete(account);
        log.info("User deleted success with account_id: {}", accountId);
    }

    private Account saveAccount(Account account) {
        try {
            return accountRepository.save(account);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Account save failed - duplicate entry: email={}", account.getEmail());
            throw new AppException(ErrorCode.ACCOUNT_EXISTED);
        }
    }

    private Account getAccountById(UUID id) {
        return accountRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
    }

    private Account getSoftDeletedAccountById(UUID id) {
        return accountRepository.findByIdAndIsDeletedTrue(id)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
    }

    private UserProfile getUserProfileByAccountId(UUID accountId) {
        return userProfileRepository.findByAccountIdAndIsDeletedFalse(accountId)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
    }

    private void softDelete(Account account) {
        account.setIsDeleted(true);
        account.setDeletedAt(Instant.now());
        account.setDeletedBy(SecurityUtil.getCurrentAccount());
    }

    private void restore(Account account) {
        account.setIsDeleted(false);
        account.setDeletedAt(null);
        account.setDeletedBy(null);
    }

}
