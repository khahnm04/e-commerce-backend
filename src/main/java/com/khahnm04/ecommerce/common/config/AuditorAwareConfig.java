package com.khahnm04.ecommerce.common.config;

import com.khahnm04.ecommerce.modules.auth.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Slf4j
@Component
public class AuditorAwareConfig implements AuditorAware<User> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @NonNull
    public Optional<User> getCurrentAuditor() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
            }

            Long userId = null;
            Object principal = authentication.getPrincipal();

            if (principal instanceof Jwt jwt) {
                userId = jwt.getClaim("userId");
            } else if (principal instanceof User user) {
                userId = user.getId();
            }

            if (userId != null) {
                return Optional.of(entityManager.getReference(User.class, userId));
            }

            return Optional.empty();

        } catch (Exception e) {
            log.warn("The current auditor could not be determined. Error: {}", e.getMessage());
            return Optional.empty();
        }
    }

}
