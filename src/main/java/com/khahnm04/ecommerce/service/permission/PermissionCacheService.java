package com.khahnm04.ecommerce.service.permission;

import com.khahnm04.ecommerce.repository.user.AccountAdminRepository;
import com.khahnm04.ecommerce.repository.auth.PermissionRepository;
import com.khahnm04.ecommerce.service.redis.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j(topic = "PERMISSION-CACHE-SERVICE")
@Service
@RequiredArgsConstructor
public class PermissionCacheService {

    private final AccountAdminRepository accountAdminRepository;
    private final PermissionRepository permissionRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisService redisService;

    private static final String PERMISSIONS_PREFIX = "admin_permissions::";
    private static final String IS_ADMIN_PREFIX = "admin_check::";

    public List<String> getPermissions(Long userId) {
        String cacheKey = PERMISSIONS_PREFIX + userId;

        @SuppressWarnings("unchecked")
        List<String> cached = (List<String>) redisService.get(cacheKey);
        if (cached != null) {
            log.info("Cache HIT permissions, userId: {}", userId);
            return cached;
        }

        List<String> permissions;
        if (isAdmin(userId)) {
            log.info("Admin with id: {}, granted all permissions", userId);
            permissions = permissionRepository.findAllPermissionCodes();
        } else {
            log.info("Account admin with id: {} loaded permissions", userId);
            permissions = accountAdminRepository.findPermissionCodesById(userId);
        }

        redisService.set(cacheKey, permissions);
        return permissions;
    }

    private boolean isAdmin(Long userId) {
        String cacheKey = IS_ADMIN_PREFIX + userId;

        Boolean cached = (Boolean) redisService.get(cacheKey);
        if (cached != null) {
            log.info("Cache HIT isAdmin, userId {}", userId);
            return cached;
        }

        boolean isAdmin = accountAdminRepository.isAdmin(userId);
        redisService.set(cacheKey, isAdmin);
        return isAdmin;
    }

    public void evictByUserId(Long userId) {
        redisService.delete(PERMISSIONS_PREFIX + userId);
        redisService.delete(IS_ADMIN_PREFIX + userId);
        log.info("Permission cache evicted for userId: {}", userId);
    }

    public void evictAll() {
        redisService.deleteByPattern(PERMISSIONS_PREFIX + "*");
        redisService.deleteByPattern(IS_ADMIN_PREFIX + "*");
        log.info("All permission caches evicted successfully");
    }

}
