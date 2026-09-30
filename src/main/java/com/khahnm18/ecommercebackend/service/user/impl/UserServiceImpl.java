package com.khahnm18.ecommercebackend.service.user.impl;

import com.khahnm18.ecommercebackend.constant.auth.AccountStatus;
import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.user.UserCreationRequest;
import com.khahnm18.ecommercebackend.dto.request.user.UserUpdateRequest;
import com.khahnm18.ecommercebackend.dto.response.user.UserResponse;
import com.khahnm18.ecommercebackend.entity.auth.Account;
import com.khahnm18.ecommercebackend.entity.user.UserProfile;
import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.mapper.auth.AccountMapper;
import com.khahnm18.ecommercebackend.mapper.user.UserMapper;
import com.khahnm18.ecommercebackend.repository.auth.AccountRepository;
import com.khahnm18.ecommercebackend.repository.user.UserProfileRepository;
import com.khahnm18.ecommercebackend.service.user.UserService;
import com.khahnm18.ecommercebackend.specification.UserSpecification;
import com.khahnm18.ecommercebackend.util.PageableUtil;
import com.khahnm18.ecommercebackend.util.SecurityUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j(topic = "USER-SERVICE")
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AccountMapper accountMapper;
    private final UserMapper userMapper;
    private final AccountRepository accountRepository;
    private final UserProfileRepository userProfileRepository;
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
    public Page<UserResponse> getAllUsers(
        int page,
        int size,
        String sort,
        String keyword,
        boolean isDeleted,
        Instant startDate,
        Instant endDate,
        AccountStatus status
    ) {
        Pageable pageable = PageableUtil.createPageable(page, size, sort);

        Specification<Account> spec = UserSpecification.byIsDeleted(isDeleted)
            .and(UserSpecification.byKeyword(keyword))
            .and(UserSpecification.byStartDate(startDate))
            .and(UserSpecification.byEndDate(endDate))
            .and(UserSpecification.byStatus(status));

        Page<Account> accountPage = accountRepository.findAll(spec, pageable);

        List<UUID> accountIds = accountPage.getContent().stream()
            .map(Account::getId)
            .toList();

        Map<UUID, UserProfile> profileMap = userProfileRepository.findAllByAccountIdIn(accountIds)
            .stream()
            .collect(Collectors.toMap(UserProfile::getAccountId, p -> p, (p1, p2) -> p1));

        return accountPage.map(account ->
            userMapper.toUserResponse(profileMap.get(account.getId()), account));
    }

    @Override
    @Transactional
    public UserResponse createUser(UserCreationRequest request) {
        Account account = accountMapper.toAccountFromUser(request);
        account.setPassword(passwordEncoder.encode(request.password()));
        account = this.saveAccount(account);

        UserProfile userProfile = userMapper.toUserProfile(request, account.getId());
        userProfile = userProfileRepository.save(userProfile);

        log.info("User profile created: fullName={}, email={}", userProfile.getFullName(), account.getEmail());
        return userMapper.toUserResponse(userProfile, account);
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID accountId, UserUpdateRequest request) {
        Account account = getAccountById(accountId);
        UserProfile userProfile = getUserProfileByAccountId(accountId);

        accountMapper.updateAccountFromUser(account, request);
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
        switch (action) {
            case "ACTIVE", "INACTIVE", "BANNED", "LOCKED" -> {
                SecurityUtil.requireAuthority("USER_UPDATE");
                List<Account> accounts = accountRepository.findAllById(accountIds);
                accounts.forEach(account -> account.setStatus(AccountStatus.valueOf(action)));
                log.info("Bulk update status {} user with accountIds: {}", action, accountIds);
            }
            case "SOFT_DELETE" -> {
                SecurityUtil.requireAuthority("USER_TRASH");
                List<Account> accounts = accountRepository.findAllById(accountIds);
                accounts.forEach(this::softDelete);
                log.info("Bulk action soft delete user with accountIds: {}", accountIds);
            }
            case "RESTORE" -> {
                SecurityUtil.requireAuthority("USER_TRASH");
                List<Account> accounts = accountRepository.findAllById(accountIds);
                accounts.forEach(this::restore);
                log.info("Bulk action restore user with accountIds: {}", accountIds);
            }
            case "DELETE" -> {
                SecurityUtil.requireAuthority("USER_DELETE");
                accountRepository.deleteAllByIdInAndIsDeletedTrue(accountIds);
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
