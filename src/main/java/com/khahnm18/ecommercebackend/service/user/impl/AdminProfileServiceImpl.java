package com.khahnm18.ecommercebackend.service.user.impl;

import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.auth.RoleRequest;
import com.khahnm18.ecommercebackend.dto.request.user.AdminCreationRequest;
import com.khahnm18.ecommercebackend.dto.response.auth.RoleResponse;
import com.khahnm18.ecommercebackend.dto.response.user.AdminCreationResponse;
import com.khahnm18.ecommercebackend.entity.auth.Account;
import com.khahnm18.ecommercebackend.entity.auth.Role;
import com.khahnm18.ecommercebackend.entity.auth.Permission;
import com.khahnm18.ecommercebackend.entity.user.AdminProfile;
import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.mapper.auth.RoleMapper;
import com.khahnm18.ecommercebackend.mapper.user.AccountMapper;
import com.khahnm18.ecommercebackend.mapper.user.AdminProfileMapper;
import com.khahnm18.ecommercebackend.repository.auth.AccountRepository;
import com.khahnm18.ecommercebackend.repository.user.AdminProfileRepository;
import com.khahnm18.ecommercebackend.service.auth.RolePermissionService;
import com.khahnm18.ecommercebackend.service.auth.RoleService;
import com.khahnm18.ecommercebackend.repository.auth.RoleRepository;
import com.khahnm18.ecommercebackend.repository.auth.RolePermissionRepository;
import com.khahnm18.ecommercebackend.service.auth.cache.PermissionCacheService;
import com.khahnm18.ecommercebackend.service.user.AdminProfileService;
import com.khahnm18.ecommercebackend.specification.RoleSpecification;
import com.khahnm18.ecommercebackend.util.PageableUtil;
import com.khahnm18.ecommercebackend.util.SecurityUtil;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j(topic = "ADMIN-PROFILE-SERVICE")
@Service
@RequiredArgsConstructor
public class AdminProfileServiceImpl implements AdminProfileService {

    private final AccountMapper accountMapper;
    private final AdminProfileMapper adminProfileMapper;
    private final AccountRepository accountRepository;
    private final AdminProfileRepository adminProfileRepository;

    @Override
    public AdminCreationResponse createAdminPrrofile(AdminCreationRequest request) {
        Account account = accountMapper.toAccount(request);
        account = accountRepository.save(account);

        AdminProfile adminProfile = adminProfileMapper.toAdminProfile(request, account.getId());
        adminProfile = adminProfileRepository.save(adminProfile);

        return null;
    }

}
