package com.khahnm04.ecommerce.config;

import com.khahnm04.ecommerce.entity.User;
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
                Object userIdClaim = jwt.getClaim("userId");
                if (userIdClaim instanceof Number number) {
                    userId = number.longValue();
                } else if (userIdClaim instanceof String s) {
                    userId = Long.parseLong(s);
                }
            } else if (principal instanceof User user) {
                userId = user.getId();
            }

            if (userId != null) {
                User auditor = entityManager.getReference(User.class, userId);
                return Optional.of(auditor);
            }

            return Optional.empty();

        } catch (Exception e) {
            log.warn("The current auditor could not be determined. Error: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public User getCurrentAuditorReference() {
        return getCurrentAuditor().orElse(null);
    }

}
