package com.khahnm04.ecommerce.security;

import com.khahnm04.ecommerce.repository.auth.PermissionRepository;
import com.khahnm04.ecommerce.repository.user.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j(topic = "JWT-AUTHENTICATION-CONVERTER-CONFIG")
@Component
@RequiredArgsConstructor
public class JwtAuthenticationConverterConfig implements Converter<Jwt, AbstractAuthenticationToken> {

    private final EmployeeRepository employeeRepository;
    private final PermissionRepository permissionRepository;

    private final JwtGrantedAuthoritiesConverter defaultAuthoritiesConverter = createDefaultConverter();
    private static JwtGrantedAuthoritiesConverter createDefaultConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("");
        return converter;
    }

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> defaultAuthorities = defaultAuthoritiesConverter.convert(jwt);

        String email = jwt.getSubject();
        List<String> permissions;
        if (employeeRepository.isSuperAdmin(email)) {
            permissions = permissionRepository.findAllPermissionCodes();
            log.info("SUPER_ADMIN detected [{}], granted all {} permissions", email, permissions.size());
        } else {
            permissions = employeeRepository.findPermissionCodesByEmail(email);
            log.info("Employee [{}] loaded {} permissions", email, permissions.size());
        }

        List<GrantedAuthority> combinedAuthorities = new ArrayList<>(defaultAuthorities);
        permissions.forEach(p -> combinedAuthorities.add(new SimpleGrantedAuthority(p)));

        return new JwtAuthenticationToken(jwt, combinedAuthorities);
    }

}
