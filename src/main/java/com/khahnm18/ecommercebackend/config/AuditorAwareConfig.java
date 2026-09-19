package com.khahnm18.ecommercebackend.config;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.jspecify.annotations.NonNull;
import java.util.Optional;
import java.util.UUID;

@Configuration
public class AuditorAwareConfig implements AuditorAware<UUID> {

    @NonNull
    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
            || authentication.getName() == null
            || !authentication.isAuthenticated()
            || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        return Optional.of(UUID.fromString(authentication.getName()));
    }

}
