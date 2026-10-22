package com.khahnm18.ecommercebackend.service.auth.impl;

import com.khahnm18.ecommercebackend.entity.auth.Account;
import com.khahnm18.ecommercebackend.entity.auth.AccountRole;
import com.khahnm18.ecommercebackend.entity.auth.Role;
import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.repository.auth.AccountRoleRepository;
import com.khahnm18.ecommercebackend.repository.auth.RoleRepository;
import com.khahnm18.ecommercebackend.service.auth.AccountRoleService;
import com.khahnm18.ecommercebackend.util.SecurityUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j(topic = "ACCOUNT-ROLE-SERVICE")
@Service
@RequiredArgsConstructor
public class AccountRoleServiceImpl implements AccountRoleService {

    private final RoleRepository roleRepository;
    private final AccountRoleRepository accountRoleRepository;

    @Override
    @Transactional
    public List<Role> assignRolesToAccount(Account account, List<UUID> roleIds) {
        List<Role> roles = getRolesByRoleIds(roleIds);
        this.saveAccountRoles(account, roles);
        return roles;
    }

    @Override
    @Transactional
    public List<Role> replaceRolesForAdmin(Account account, List<UUID> newRoleIds) {
        List<Role> newRoles = getRolesByRoleIds(newRoleIds);
        Set<UUID> newIds = newRoles.stream().map(Role::getId).collect(Collectors.toSet());

        List<AccountRole> current = accountRoleRepository.findByAccountId(account.getId());
        Set<UUID> currentIds = current.stream().map(AccountRole::getRoleId).collect(Collectors.toSet());

        accountRoleRepository.deleteAllInBatch(current.stream()
            .filter(ar -> !newIds.contains(ar.getRoleId()))
            .toList());

        saveAccountRoles(account, newRoles.stream()
            .filter(role -> !currentIds.contains(role.getId()))
            .toList());

        if (!currentIds.equals(newIds)) {
            account.setUpdatedAt(Instant.now());
            account.setUpdatedBy(SecurityUtil.getCurrentAccount());
        }
        return newRoles;
    }

    private List<Role> getRolesByRoleIds(List<UUID> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Role> roles = roleRepository.findAllById(roleIds);
        if (roles.size() != roleIds.size()) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
        return roles;
    }

    private void saveAccountRoles(Account account, List<Role> roles) {
        List<AccountRole> accountRoles = roles.stream()
            .map(role -> AccountRole.builder()
                .accountId(account.getId())
                .roleId(role.getId())
                .build())
            .toList();
        accountRoleRepository.saveAll(accountRoles);
        log.info("Assigned {} roles to account: {}", accountRoles.size(), account.getId());
    }

}
