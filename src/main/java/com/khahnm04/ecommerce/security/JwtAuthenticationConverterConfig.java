package com.khahnm04.ecommerce.security;

import com.khahnm04.ecommerce.service.permission.PermissionCacheService;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j(topic = "JWT-AUTHENTICATION-CONVERTER")
@Component
@RequiredArgsConstructor
public class JwtAuthenticationConverterConfig implements Converter<Jwt, AbstractAuthenticationToken> {

    private final PermissionCacheService permissionCacheService;
    private final JwtGrantedAuthoritiesConverter defaultAuthoritiesConverter = createDefaultConverter();

    private static JwtGrantedAuthoritiesConverter createDefaultConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("");
        return converter;
    }

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> defaultAuthorities = defaultAuthoritiesConverter.convert(jwt);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            String uri = attributes.getRequest().getRequestURI();

            if (uri.startsWith("/api/v1/admin")) {
                Long userId = Long.valueOf(jwt.getSubject());

                List<String> permissions = permissionCacheService.getPermissions(userId);
                log.info("Loaded {} permissions for Admin with Id: {}", permissions.size(), userId);

                List<GrantedAuthority> combinedAuthorities = new ArrayList<>(defaultAuthorities);
                permissions.forEach(p -> combinedAuthorities.add(new SimpleGrantedAuthority(p)));
                return new JwtAuthenticationToken(jwt, combinedAuthorities);
            }
        }

        return new JwtAuthenticationToken(jwt, defaultAuthorities);
    }

}
