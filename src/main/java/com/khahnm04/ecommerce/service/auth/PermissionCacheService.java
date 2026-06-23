package com.khahnm04.ecommerce.service.auth;

import com.khahnm04.ecommerce.repository.user.AccountAdminRepository;
import com.khahnm04.ecommerce.repository.auth.PermissionRepository;
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

    private static final String PERMISSIONS_PREFIX = "admin_permissions::";
    private static final String IS_ADMIN_PREFIX = "admin_check::";

    public List<String> getPermissions(String email) {
        String cacheKey = PERMISSIONS_PREFIX + email;

        @SuppressWarnings("unchecked")
        List<String> cached = (List<String>) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            log.info("Cache HIT permissions [{}]", email);
            return cached;
        }

        List<String> permissions;
        if (isAdmin(email)) {
            log.info("SUPER_ADMIN [{}], granted all permissions", email);
            permissions = permissionRepository.findAllPermissionCodes();
        } else {
            log.info("Account admin [{}] loaded permissions", email);
            permissions = accountAdminRepository.findPermissionCodesByEmail(email);
        }

        redisTemplate.opsForValue().set(PERMISSIONS_PREFIX + email, permissions);
        return permissions;
    }

    public boolean isAdmin(String email) {
        String cacheKey = IS_ADMIN_PREFIX + email;

        Boolean cached = (Boolean) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            log.info("Cache HIT isAdmin [{}]", email);
            return cached;
        }

        boolean isAdmin = accountAdminRepository.isAdmin(email);
        redisTemplate.opsForValue().set(cacheKey, isAdmin);
        return isAdmin;
    }

    public void evictByEmail(String email) {
        redisTemplate.delete(PERMISSIONS_PREFIX + email);
        redisTemplate.delete(IS_ADMIN_PREFIX + email);
        log.info("Cache evicted [{}]", email);
    }

    public void evictAll() {
        redisTemplate.delete(redisTemplate.keys(PERMISSIONS_PREFIX + "*"));
        redisTemplate.delete(redisTemplate.keys(IS_ADMIN_PREFIX + "*"));
        log.info("All permission cache evicted");
    }

}
