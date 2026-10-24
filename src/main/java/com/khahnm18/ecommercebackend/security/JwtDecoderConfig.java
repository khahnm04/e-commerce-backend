package com.khahnm18.ecommercebackend.security;

import com.khahnm18.ecommercebackend.config.properties.JwtProperties;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.jspecify.annotations.NonNull;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtDecoderConfig implements JwtDecoder {

    private final JwtProperties jwtProperties;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @PostConstruct
    public void init() {
        if(Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(jwtProperties.getSecretKey().getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
        }
    }

    @NonNull
    @Override
    public Jwt decode(@NonNull String token) throws JwtException {
        return nimbusJwtDecoder.decode(token);
    }

}
