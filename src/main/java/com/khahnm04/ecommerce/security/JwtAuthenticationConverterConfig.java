package com.khahnm04.ecommerce.security;

import com.khahnm04.ecommerce.repository.user.EmployeeRepository;
import lombok.RequiredArgsConstructor;
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

@Component
@RequiredArgsConstructor
public class JwtAuthenticationConverterConfig implements Converter<Jwt, AbstractAuthenticationToken> {

    private final EmployeeRepository employeeRepository;

    // Tạo 1 lần duy nhất khi class được khởi tạo
    private final JwtGrantedAuthoritiesConverter defaultAuthoritiesConverter = createDefaultConverter();
    private static JwtGrantedAuthoritiesConverter createDefaultConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("");
        return converter;
    }

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        // 1. Dùng lại object đã tạo sẵn ở trên
        Collection<GrantedAuthority> defaultAuthorities = defaultAuthoritiesConverter.convert(jwt);

        // 2. Lấy email từ subject của JWT
        String email = jwt.getSubject();

        // 3. Query danh sách quyền (permissions) từ Database
        List<String> permissions = employeeRepository.findPermissionCodesByEmail(email);

        // 4. Gộp lại
        List<GrantedAuthority> combinedAuthorities = new ArrayList<>(defaultAuthorities);
        permissions.forEach(p -> combinedAuthorities.add(new SimpleGrantedAuthority(p)));

        return new JwtAuthenticationToken(jwt, combinedAuthorities);
    }
}
