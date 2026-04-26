package com.khahnm04.ecommerce.config;

import com.khahnm04.ecommerce.entity.Employee;
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
public class AuditorAwareConfig implements AuditorAware<Employee> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @NonNull
    public Optional<Employee> getCurrentAuditor() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
            }

            Object principal = authentication.getPrincipal();

            if (principal instanceof Jwt jwt) {
                String userType = jwt.getClaim("userType");
                if (!"EMPLOYEE".equals(userType)) {
                    return Optional.empty();
                }

                Long userId = null;
                Object userIdClaim = jwt.getClaim("userId");
                if (userIdClaim instanceof Number number) {
                    userId = number.longValue();
                } else if (userIdClaim instanceof String s) {
                    userId = Long.parseLong(s);
                }

                if (userId != null) {
                    Employee auditor = entityManager.getReference(Employee.class, userId);
                    return Optional.of(auditor);
                }
            } else if (principal instanceof Employee employee) {
                return Optional.of(employee);
            }

            return Optional.empty();

        } catch (Exception e) {
            log.warn("The current auditor could not be determined. Error: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Employee getCurrentAuditorReference() {
        return getCurrentAuditor().orElse(null);
    }

}
