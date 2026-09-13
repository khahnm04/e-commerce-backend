package com.khahnm18.ecommercebackend.config;

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
        return authentication != null && authentication.getName() != null
            ? Optional.of(UUID.fromString(authentication.getName()))
            : Optional.empty();
    }

}
