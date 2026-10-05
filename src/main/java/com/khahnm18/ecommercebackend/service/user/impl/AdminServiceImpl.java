package com.khahnm18.ecommercebackend.service.user.impl;

import com.khahnm18.ecommercebackend.enums.AccountStatus;
import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.user.*;
import com.khahnm18.ecommercebackend.dto.response.user.MyProfileAdminResponse;
import com.khahnm18.ecommercebackend.dto.response.user.AdminResponse;
import com.khahnm18.ecommercebackend.entity.auth.Account;
import com.khahnm18.ecommercebackend.entity.auth.Role;
import com.khahnm18.ecommercebackend.entity.user.AdminProfile;
import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.mapper.AccountMapper;
import com.khahnm18.ecommercebackend.mapper.AdminMapper;
import com.khahnm18.ecommercebackend.repository.auth.AccountRepository;
import com.khahnm18.ecommercebackend.repository.auth.RoleRepository;
import com.khahnm18.ecommercebackend.repository.user.AdminProfileRepository;
import com.khahnm18.ecommercebackend.service.auth.AccountRoleService;
import com.khahnm18.ecommercebackend.service.user.AdminService;
import com.khahnm18.ecommercebackend.specification.AdminSpecification;
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
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j(topic = "ADMIN-SERVICE")
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AccountRoleService accountRoleService;
    private final AccountMapper accountMapper;
    private final AdminMapper adminMapper;
    private final AccountRepository accountRepository;
    private final AdminProfileRepository adminProfileRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AdminResponse getAdminDetailById(UUID accountId, boolean isDeleted) {
        Account account = isDeleted
            ? this.getSoftDeletedAccountById(accountId)
            : this.getAccountById(accountId);

        AdminProfile adminProfile = getAdminProfileByAccountId(accountId);
        List<Role> roles = roleRepository.findRolesByAccountId(accountId);
        return adminMapper.toAdminResponseWithRoles(adminProfile, account, roles);
    }

    @Override
    public Page<AdminResponse> getAllAdmins(
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

        Specification<Account> spec = AdminSpecification.isAdminAccount()
            .and(UserSpecification.byIsDeleted(isDeleted))
            .and(AdminSpecification.byKeyword(keyword))
            .and(AdminSpecification.byStartDate(startDate))
            .and(AdminSpecification.byEndDate(endDate))
            .and(AdminSpecification.byStatus(status));

        Page<Account> accountPage = accountRepository.findAll(spec, pageable);

        List<UUID> accountIds = accountPage.getContent().stream()
            .map(Account::getId)
            .toList();

        Map<UUID, AdminProfile> profileMap = adminProfileRepository.findAllByAccountIdIn(accountIds)
            .stream()
            .collect(Collectors.toMap(AdminProfile::getAccountId, p -> p, (p1, p2) -> p1));

        return accountPage.map(account ->
            adminMapper.toAdminResponse(profileMap.get(account.getId()), account));
    }

    @Override
    public MyProfileAdminResponse getMyProfile() {
        UUID accountId = SecurityUtil.getCurrentAccount();
        Account account = this.getAccountById(accountId);
        AdminProfile adminProfile = this.getAdminProfileByAccountId(account.getId());
        return adminMapper.toAdminProfileResponse(adminProfile, account);
    }

    @Override
    @Transactional
    public AdminResponse createAdmin(AdminCreationRequest request) {
        Account account = accountMapper.toAccount(request);
        account.setPassword(passwordEncoder.encode(request.password()));
        account = this.saveAccount(account);

        AdminProfile adminProfile = adminMapper.toAdminProfile(request, account.getId());
        adminProfile = adminProfileRepository.save(adminProfile);

        List<Role> roles = accountRoleService.assignRolesToAccount(account, request.roles());
        log.info("Admin profile created: fullName={}, email={}", adminProfile.getFullName(), account.getEmail());
        return adminMapper.toAdminResponseWithRoles(adminProfile, account, roles);
    }

    @Override
    @Transactional
    public AdminResponse updateAdmin(UUID accountId, AdminUpdateRequest request) {
        Account account = getAccountById(accountId);
        AdminProfile adminProfile = getAdminProfileByAccountId(accountId);

        accountMapper.updateAccount(account, request);
        account = this.saveAccount(account);

        adminMapper.updateAdmin(adminProfile, request);
        adminProfile = adminProfileRepository.save(adminProfile);

        List<Role> roles = accountRoleService.replaceRolesForAdmin(account, request.roles());
        log.info("Admin profile updated: accountId={}, email={}", accountId, account.getEmail());
        return adminMapper.toAdminResponseWithRoles(adminProfile, account, roles);
    }

    @Override
    @Transactional
    public MyProfileAdminResponse updateMyProfileAdmin(MyProfileAdminRequest request) {
        UUID accountId = SecurityUtil.getCurrentAccount();
        Account account = this.getAccountById(accountId);
        AdminProfile adminProfile = this.getAdminProfileByAccountId(account.getId());
        accountMapper.updateAccount(account, request);
        adminMapper.updateAdminProfile(adminProfile, request);
        log.info("My profile Admin updated: accountId={}, email={}", accountId, account.getEmail());
        return adminMapper.toAdminProfileResponse(adminProfile, account);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        UUID accountId = SecurityUtil.getCurrentAccount();
        Account account = this.getAccountById(accountId);
        if (!passwordEncoder.matches(request.oldPassword(), account.getPassword())) {
            throw new AppException(ErrorCode.OLD_PASSWORD_INCORRECT);
        }
        if (passwordEncoder.matches(request.newPassword(), account.getPassword())) {
            throw new AppException(ErrorCode.NEW_PASSWORD_SAME_AS_OLD);
        }
        account.setPassword(passwordEncoder.encode(request.newPassword()));
    }

    @Override
    @Transactional
    public void softDeleteAdmin(UUID accountId) {
        Account account = getAccountById(accountId);
        this.softDelete(account);
        log.info("Admin soft deleted with account_id: {}", accountId);
    }

    @Override
    @Transactional
    public void restoreAdmin(UUID accountId) {
        Account account = getSoftDeletedAccountById(accountId);
        this.restore(account);
        log.info("Admin restored with account_id: {}", accountId);
    }

    @Override
    @Transactional
    public void bulkAction(BulkActionRequest request) {
        Set<UUID> accountIds = request.ids();
        String action = request.action().toUpperCase();
        switch (action) {
            case "ACTIVE", "INACTIVE", "BANNED", "LOCKED" -> {
                SecurityUtil.requireAuthority("ADMIN_UPDATE");
                List<Account> accounts = accountRepository.findAllById(accountIds);
                accounts.forEach(account -> account.setStatus(AccountStatus.valueOf(action)));
                log.info("Bulk update status {} admin with accountIds: {}", action, accountIds);
            }
            case "SOFT_DELETE" -> {
                SecurityUtil.requireAuthority("ADMIN_TRASH");
                List<Account> accounts = accountRepository.findAllById(accountIds);
                accounts.forEach(this::softDelete);
                log.info("Bulk action soft delete admin with accountIds: {}", accountIds);
            }
            case "RESTORE" -> {
                SecurityUtil.requireAuthority("ADMIN_TRASH");
                List<Account> accounts = accountRepository.findAllById(accountIds);
                accounts.forEach(this::restore);
                log.info("Bulk action restore admin with accountIds: {}", accountIds);
            }
            case "DELETE" -> {
                SecurityUtil.requireAuthority("ADMIN_DELETE");
                accountRepository.deleteAllByIdInAndIsDeletedTrue(accountIds);
                log.info("Bulk action delete admin with accountIds: {}", accountIds);
            }
        }
    }

    @Override
    @Transactional
    public void resetPassword(UUID accountId, ResetPasswordRequest request) {
        Account account = getAccountById(accountId);
        account.setPassword(passwordEncoder.encode(request.password()));
        log.info("Admin reset password success with account_id: {}", accountId);
    }

    @Override
    @Transactional
    public void deleteAdmin(UUID accountId) {
        Account account = getSoftDeletedAccountById(accountId);
        accountRepository.delete(account);
        log.info("Admin deleted success with account_id: {}", accountId);
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

    private AdminProfile getAdminProfileByAccountId(UUID accopuntId) {
        return adminProfileRepository.findByAccountIdAndIsDeletedFalse(accopuntId)
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
