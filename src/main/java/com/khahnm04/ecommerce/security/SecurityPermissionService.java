package com.khahnm04.ecommerce.security;

import com.khahnm04.ecommerce.entity.User;
import com.khahnm04.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service để check permissions từ database (real-time),
 * thay vì đọc từ JWT scope (stale).
 * <p>
 * Sử dụng trong @PreAuthorize:
 * {@code @PreAuthorize("@securityPermissionService.hasPermission('category:view')")}
 */
@Service("Perm")
@RequiredArgsConstructor
public class SecurityPermissionService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public boolean hasPermission(String permissionCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Long userId = extractUserId(authentication);
        if (userId == null) {
            return false;
        }

        User user = userRepository.findByIdWithRolesAndPermissions(userId).orElse(null);
        if (user == null || user.getRoles() == null) {
            return false;
        }

        Set<String> permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> permission.getCode())
                .collect(Collectors.toSet());

        return permissions.contains(permissionCode);
    }

    private Long extractUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof Jwt jwt) {
            Object userIdClaim = jwt.getClaim("userId");
            if (userIdClaim instanceof Number number) {
                return number.longValue();
            } else if (userIdClaim instanceof String s) {
                return Long.parseLong(s);
            }
        } else if (principal instanceof User user) {
            return user.getId();
        }

        return null;
    }

    @Transactional(readOnly = true)
    public boolean hasPermissionForBulkAction(String module, String action) {
        if (action == null) return false;
        String upperAction = action.toUpperCase();
        
        if (upperAction.equals("SOFT_DELETE") || upperAction.equals("DELETE") || upperAction.equals("RESTORE")) {
            return hasPermission(module + ":delete");
        }
    
        return hasPermission(module + ":update");
    }
    
}
