package com.khahnm18.ecommercebackend.service.auth.cache;

import com.khahnm18.ecommercebackend.repository.auth.AccountRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.UUID;

@Slf4j(topic = "ACCOUNT-CACHE-SERVICE")
@Service
@RequiredArgsConstructor
public class AccountCacheService {

    private final AccountRepository accountRepository;

    @Cacheable(cacheNames = "admin_check", key = "#accountId")
    public boolean isAdmin(UUID accountId) {
        return accountRepository.isAdmin(accountId);
    }

}
