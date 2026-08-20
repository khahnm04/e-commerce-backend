package com.khahnm04.ecommerce.service.user;

import com.khahnm04.ecommerce.constant.user.AccountAdminStatus;
import com.khahnm04.ecommerce.dto.projection.AccountAdminProjection;
import com.khahnm04.ecommerce.dto.request.user.*;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.response.role.RoleResponse;
import com.khahnm04.ecommerce.dto.response.user.AccountAdminResponse;
import com.khahnm04.ecommerce.dto.response.user.AdminProfileResponse;
import com.khahnm04.ecommerce.entity.auth.AccountAdminRole;
import com.khahnm04.ecommerce.entity.auth.Role;
import com.khahnm04.ecommerce.entity.user.AccountAdmin;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.auth.RoleMapper;
import com.khahnm04.ecommerce.mapper.user.AccountAdminMapper;
import com.khahnm04.ecommerce.repository.auth.AccountAdminRoleRepository;
import com.khahnm04.ecommerce.repository.auth.RoleRepository;
import com.khahnm04.ecommerce.repository.user.AccountAdminRepository;
import com.khahnm04.ecommerce.service.permission.PermissionCacheService;
import com.khahnm04.ecommerce.specification.AccountAdminSpecification;
import com.khahnm04.ecommerce.util.PageableUtil;
import com.khahnm04.ecommerce.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.stream.Collectors;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Slf4j(topic = "ACCOUNT-ADMIN-SERVICE")
@Service
@RequiredArgsConstructor
public class AccountAdminServiceImpl implements AccountAdminService {

    private final AccountAdminRepository accountAdminRepository;
    private final AccountAdminRoleRepository accountAdminRoleRepository;
    private final RoleRepository roleRepository;
    private final AccountAdminMapper accountAdminMapper;
    private final RoleMapper roleMapper;
    private final PermissionCacheService permissionCacheService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AccountAdminResponse createAccountAdmin(AccountAdminCreationRequest request) {
        if (accountAdminRepository.existsByPhoneNumberAndIsDeletedFalse(request.phoneNumber())) {
            throw new AppException(ErrorCode.ACCOUNT_ADMIN_EXISTED);
        }
        if (accountAdminRepository.existsByEmailAndIsDeletedFalse(request.email())) {
            throw new AppException(ErrorCode.ACCOUNT_ADMIN_EXISTED);
        }
        AccountAdmin accountAdmin = accountAdminMapper.toAccountAdmin(request);
        accountAdmin = accountAdminRepository.save(accountAdmin);
        List<Role> roles = assignRolesToAccountAdmin(accountAdmin, request.roles());

        Set<RoleResponse> roleResponses = roles.stream()
            .map(role -> roleMapper.toRoleResponse(role, Set.of()))
            .collect(Collectors.toSet());

        log.info("Account admin created: name={}", accountAdmin.getFullName());
        return accountAdminMapper.toAccountAdminResponse(accountAdmin, roleResponses);
    }

    @Override
    public AccountAdminResponse getAccountAdminDetailById(Long id, boolean isDeleted) {
        AccountAdmin accountAdmin = isDeleted ? getSoftDeletedAccountAdminById(id) : getAccountAdminById(id);
        Set<RoleResponse> roles = accountAdminRoleRepository.findRolesByAccountAdminId(accountAdmin.getId())
            .stream()
            .map(role -> roleMapper.toRoleResponse(role, null))
            .collect(Collectors.toSet());
        return accountAdminMapper.toAccountAdminResponse(accountAdmin, roles);
    }

    @Override
    public Page<AccountAdminResponse> getAllAccountAdmins(AccountAdminFilterRequest request) {
        Pageable pageable = PageableUtil.createPageable(
            request.page(),
            request.size(),
            request.sort()
        );
        Page<AccountAdminProjection> accountAdminPage = accountAdminRepository.findBy(
            AccountAdminSpecification.buildQuery(request, request.isDeleted()),
            query -> query.as(AccountAdminProjection.class).page(pageable)
        );
        return accountAdminPage.map(accountAdminMapper::toAccountAdminResponse);
    }

    @Override
    @Transactional
    public AccountAdminResponse updateAccountAdmin(Long id, AccountAdminUpdateRequest request) {
        if (accountAdminRepository.existsByPhoneNumberAndIdNotAndIsDeletedFalse(request.phoneNumber(), id)) {
            throw new AppException(ErrorCode.ACCOUNT_ADMIN_EXISTED);
        }
        if (accountAdminRepository.existsByEmailAndIdNotAndIsDeletedFalse(request.email(), id)) {
            throw new AppException(ErrorCode.ACCOUNT_ADMIN_EXISTED);
        }

        AccountAdmin accountAdmin = getAccountAdminById(id);
        accountAdminMapper.updateAccountAdmin(accountAdmin, request);
        List<Role> roles = replaceRolesForAccountAdmin(accountAdmin, request.roles());

        Set<RoleResponse> roleResponses = roles.stream()
            .map(role -> roleMapper.toRoleResponse(role, null))
            .collect(Collectors.toSet());

        log.info("Account admin updated: name={}", accountAdmin.getFullName());
        return accountAdminMapper.toAccountAdminResponse(accountAdmin, roleResponses);
    }

    @Override
    @Transactional
    public void softDeleteAccountAdmin(Long id) {
        AccountAdmin accountAdmin = getAccountAdminById(id);
        accountAdmin.setIsDeleted(true);
        accountAdmin.setDeletedAt(Instant.now());
        accountAdmin.setDeletedBy(accountAdmin.getFullName());
        accountAdminRepository.save(accountAdmin);
        permissionCacheService.evictByUserId(accountAdmin.getId());
        log.info("Soft-deleted account admin with id: {}", id);
    }

    @Override
    @Transactional
    public void deleteAccountAdmin(Long id) {
        AccountAdmin accountAdmin = getSoftDeletedAccountAdminById(id);
        accountAdminRoleRepository.deleteByAccountAdminId(id);
        accountAdminRepository.delete(accountAdmin);
        permissionCacheService.evictByUserId(accountAdmin.getId());
        log.info("Deleted account admin with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreAccountAdmin(Long id) {
        AccountAdmin accountAdmin = getSoftDeletedAccountAdminById(id);
        accountAdmin.setIsDeleted(false);
        accountAdmin.setDeletedAt(null);
        accountAdmin.setDeletedBy(null);
        accountAdminRepository.save(accountAdmin);
        permissionCacheService.evictByUserId(accountAdmin.getId());
        log.info("Account admin restored with id: {}", id);
    }

    @Override
    @Transactional
    public void bulkAction(BulkActionRequest request) {
        Set<Long> accountAdminIds = request.ids();
        String action = request.action().toUpperCase();
        switch (action) {
            case "ACTIVE", "INACTIVE", "BANNED", "LOCKED" -> {
                accountAdminRepository.updateStatusAllByIds(AccountAdminStatus.valueOf(action), accountAdminIds);
                log.info("Bulk update status {} account admins ids: {}", action, accountAdminIds);
            }
            case "SOFT_DELETE" -> {
                Long userId = SecurityUtil.getCurrentUserId();
                AccountAdmin accountAdmin = getAccountAdminById(userId);
                accountAdminRepository.softDeleteAllByIds(accountAdminIds, accountAdmin.getFullName());
                permissionCacheService.evictAll();
                log.info("Bulk action soft delete account admins ids: {}", accountAdminIds);
            }
            case "RESTORE" -> {
                accountAdminRepository.restoreAllByIds(accountAdminIds);
                permissionCacheService.evictAll();
                log.info("Bulk action restore account admins ids: {}", accountAdminIds);
            }
            case "DELETE" -> {
                accountAdminRoleRepository.deleteAllByAccountAdminIds(accountAdminIds);
                accountAdminRepository.deleteAllByIds(accountAdminIds);
                permissionCacheService.evictAll();
                log.info("Bulk action delete account admins ids: {}", accountAdminIds);
            }
        }
    }

    @Override
    public AdminProfileResponse getMyProfile() {
        Long userId = SecurityUtil.getCurrentUserId();
        AccountAdmin accountAdmin = getAccountAdminById(userId);
        return accountAdminMapper.toAdminProfileResponse(accountAdmin);
    }

    @Override
    @Transactional
    public AdminProfileResponse updateMyProfile(AdminProfileRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        AccountAdmin accountAdmin = getAccountAdminById(userId);
        accountAdminMapper.updateAdminProfile(accountAdmin, request);
        accountAdminRepository.save(accountAdmin);
        return accountAdminMapper.toAdminProfileResponse(accountAdmin);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        AccountAdmin accountAdmin = getAccountAdminById(userId);
        accountAdmin.setPassword(passwordEncoder.encode(request.password()));
        accountAdminRepository.save(accountAdmin);
    }

    @Override
    public List<String> getMyPermissions() {
        Long userId = SecurityUtil.getCurrentUserId();
        return permissionCacheService.getPermissions(userId);
    }

    private AccountAdmin getAccountAdminById(Long id) {
        return accountAdminRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_ADMIN_NOT_FOUND));
    }

    private AccountAdmin getSoftDeletedAccountAdminById(Long id) {
        return accountAdminRepository.findByIdAndIsDeletedTrue(id)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_ADMIN_NOT_FOUND));
    }

    private AccountAdmin getAccountAdminByEmail(String email) {
        return accountAdminRepository.findByEmailAndIsDeletedFalse(email)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_ADMIN_NOT_FOUND));
    }

    private List<Role> assignRolesToAccountAdmin(AccountAdmin accountAdmin, Set<Long> roleIds) {
        List<Role> roles = roleRepository.findAllById(roleIds);
        if (roles.size() != roleIds.size()) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }

        List<AccountAdminRole> accountAdminRoles = roles.stream()
            .map(role -> AccountAdminRole.builder()
                .accountAdminId(accountAdmin.getId())
                .roleId(role.getId())
                .build())
            .collect(Collectors.toList());

        accountAdminRoleRepository.saveAll(accountAdminRoles);
        log.info("Assigned {} roles to accountAdminId={}", accountAdminRoles.size(), accountAdmin.getId());
        return roles;
    }

    private List<Role> replaceRolesForAccountAdmin(AccountAdmin accountAdmin, Set<Long> roleIds) {
        List<Role> roles = roleRepository.findAllById(roleIds);
        if (roles.size() != roleIds.size()) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }

        List<AccountAdminRole> existingAccountAdminRoles = accountAdminRoleRepository.findByAccountAdminId(accountAdmin.getId());
        Set<Long> existingRoleIds = existingAccountAdminRoles.stream()
            .map(AccountAdminRole::getRoleId)
            .collect(Collectors.toSet());

        if (existingRoleIds.equals(roleIds)) {
            return roles;
        }

        List<Long> toDeleteRoleIds = existingAccountAdminRoles.stream()
            .filter(ar -> !roleIds.contains(ar.getRoleId()))
            .map(AccountAdminRole::getId)
            .toList();

        List<Long> toAddRoleIds = roleIds.stream()
            .filter(roleId -> !existingRoleIds.contains(roleId))
            .toList();

        if (!toDeleteRoleIds.isEmpty()) {
            accountAdminRoleRepository.deleteAllByIds(toDeleteRoleIds);
        }
        if (!toAddRoleIds.isEmpty()) {
            accountAdminRoleRepository.insertAll(accountAdmin.getId(), toAddRoleIds);
        }
        permissionCacheService.evictByUserId(accountAdmin.getId());
        return roles;
    }

}
