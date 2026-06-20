package com.khahnm04.ecommerce.security;

import com.khahnm04.ecommerce.service.auth.PermissionCacheService;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "JWT-AUTHENTICATION-CONVERTER")
@Component
public class JwtAuthenticationConverterConfig implements Converter<Jwt, AbstractAuthenticationToken> {

    private final LoadingCache<String, List<String>> localCache;

    private final JwtGrantedAuthoritiesConverter defaultAuthoritiesConverter = createDefaultConverter();
    private static JwtGrantedAuthoritiesConverter createDefaultConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("");
        return converter;
    }

    public JwtAuthenticationConverterConfig(PermissionCacheService permissionCacheService) {
        this.localCache = Caffeine.newBuilder()
            .expireAfterWrite(500, TimeUnit.MILLISECONDS)
            .maximumSize(500)
            .build(permissionCacheService::getPermissions);
    }

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> defaultAuthorities = defaultAuthoritiesConverter.convert(jwt);
        String email = jwt.getSubject();

        List<String> permissions = localCache.get(email);
        log.info("Loaded {} permissions for [{}]", permissions.size(), email);

        List<GrantedAuthority> combinedAuthorities = new ArrayList<>(defaultAuthorities);
        permissions.forEach(p -> combinedAuthorities.add(new SimpleGrantedAuthority(p)));
        return new JwtAuthenticationToken(jwt, combinedAuthorities);
    }

}
