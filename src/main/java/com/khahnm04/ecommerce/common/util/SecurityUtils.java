package com.khahnm04.ecommerce.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import com.khahnm04.ecommerce.entity.Employee;
import com.khahnm04.ecommerce.entity.Customer;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;

@Slf4j
public class SecurityUtils {

    private SecurityUtils() {}

    public static String extractPrincipal() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                return null;
            }
            if(authentication.getPrincipal() instanceof UserDetails userDetails) {
                return userDetails.getUsername();
            }
            if(authentication.getPrincipal() instanceof Jwt jwt) {
                return jwt.getSubject();
            }
            if (authentication.getPrincipal() instanceof  String s) {
                return s;
            }
        } catch (Exception e) {
            log.error("Error getting current user", e);
        }
        return null;
    }

    public static Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) {
            Object userIdClaim = jwt.getClaim("userId");
            if (userIdClaim instanceof Number number) {
                return number.longValue();
            } else if (userIdClaim instanceof String s) {
                return Long.parseLong(s);
            }
        } else if (principal instanceof Employee employee) {
            return employee.getId();
        } else if (principal instanceof Customer customer) {
            return customer.getId();
        }
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

}
