package com.khahnm18.ecommercebackend.service.user.impl;

import com.khahnm18.ecommercebackend.entity.auth.Account;
import com.khahnm18.ecommercebackend.entity.auth.AccountRole;
import com.khahnm18.ecommercebackend.entity.auth.Role;
import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.repository.auth.AccountRoleRepository;
import com.khahnm18.ecommercebackend.repository.auth.RoleRepository;
import com.khahnm18.ecommercebackend.service.user.AccountRoleService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j(topic = "ACCOUNT-ROLE-SERVICE")
@Service
@RequiredArgsConstructor
public class AccountRoleServiceImpl implements AccountRoleService {

    private final RoleRepository roleRepository;
    private final AccountRoleRepository accountRoleRepository;

    @Override
    @Transactional
    public List<Role> assignRolesToAccount(UUID accountId, List<UUID> roleIds) {
        List<Role> roles = validateRoles(roleIds);

        List<AccountRole> accountRoles = roles.stream()
            .map(role -> AccountRole.builder()
                .accountId(accountId)
                .roleId(role.getId())
                .build())
            .toList();

        accountRoleRepository.saveAll(accountRoles);
        log.info("Assigned {} roles to account: {}", accountRoles.size(), accountId);
        return roles;
    }

    @Override
    @Transactional
    public List<Role> replaceRolesForAdmin(UUID accountId, List<UUID> roleIds) {
        List<Role> roles = validateRoles(roleIds);
        List<AccountRole> existingAccountRoles = accountRoleRepository.findByAccountId(accountId);

        List<UUID> existingRoleIds = existingAccountRoles.stream()
            .map(AccountRole::getRoleId)
            .toList();

        if (existingRoleIds.equals(roleIds)) {
            return roles;
        }

        List<UUID> toRemove = existingAccountRoles.stream()
            .filter(accountRole -> !roleIds.contains(accountRole.getRoleId()))
            .map(AccountRole::getId)
            .toList();

        List<UUID> toAdd = roleIds.stream()
            .filter(roleId -> !existingRoleIds.contains(roleId))
            .toList();

        if (!toRemove.isEmpty()) {
            accountRoleRepository.deleteAllByIds(toRemove);
        }
        if (!toAdd.isEmpty()) {
            accountRoleRepository.insertAll(accountId, toAdd);
        }
        return roles;
    }

    private List<Role> validateRoles(List<UUID> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Role> roles = roleRepository.findAllById(roleIds);
        if (roles.size() != roleIds.size()) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
        return roles;
    }

}
