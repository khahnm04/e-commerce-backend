package com.khahnm18.ecommercebackend.service.auth.cache;

import com.khahnm18.ecommercebackend.entity.auth.Permission;
import com.khahnm18.ecommercebackend.repository.auth.AccountRepository;
import com.khahnm18.ecommercebackend.repository.auth.PermissionRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.UUID;

@Slf4j(topic = "PERMISSION-CACHE-SERVICE")
@Service
@RequiredArgsConstructor
public class PermissionCacheService {

    private final AccountCacheService accountCacheService;
    private final AccountRepository accountRepository;
    private final PermissionRepository permissionRepository;

    @Cacheable(cacheNames = "admin_perms", key = "#accountId")
    public List<String> getPermissions(UUID accountId) {
        if (accountCacheService.isAdmin(accountId)) {
            return permissionRepository.findAll()
                .stream()
                .map(Permission::getCode)
                .toList();
        }
        return accountRepository.findAllPermissionCodesByAccountId(accountId);
    }

    @CacheEvict(cacheNames = {"admin_perms", "admin_check"}, key = "#accountId")
    public void evictByAccountId(UUID accountId) {
        log.info("Permission cache evicted for accountId: {}", accountId);
    }

    @CacheEvict(cacheNames = {"admin_perms", "admin_check"}, allEntries = true)
    public void evictAll() {
        log.info("All permission caches evicted successfully");
    }

}
