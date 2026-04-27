package com.khahnm04.ecommerce.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.khahnm04.ecommerce.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.khahnm04.ecommerce.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.khahnm04.ecommerce.security.oauth2.OAuth2AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableJpaAuditing(auditorAwareRef = "auditorAwareConfig")
public class SecurityConfig {

    private static final String[] ADMIN_PUBLIC_POST_ENDPOINTS = {
            "/api/v1/auth/admin/login",
            "/api/v1/auth/admin/introspect"
    };

    private static final String[] CLIENT_PUBLIC_POST_ENDPOINTS = {
            "/api/v1/auth/client/login",
            "/api/v1/auth/client/register",
            "/api/v1/auth/client/introspect",
            "/api/v1/auth/client/send-otp",
            "/api/v1/auth/client/verify-otp",
            "/api/v1/auth/client/forgot-password/verify-otp",
            "/api/v1/auth/client/forgot-password/reset",
            "/api/v1/customers"
    };    
    
    private static final String[] CLIENT_PUBLIC_GET_ENDPOINTS = {
            "/api/v1/upload/signature",
            "/oauth2/**",
            "/login/oauth2/code/**"
    };

    private final JwtDecoderConfig jwtDecoderConfig;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeUserDetailsService employeeUserDetailsService;
    private final CustomerUserDetailsService customerUserDetailsService;
    private final CookieBearerTokenResolver cookieBearerTokenResolver;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    // ======================== Admin Filter Chain ========================

    @Bean
    @Order(1)
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/v1/admin/**", "/api/v1/auth/admin/**")

            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(
                    request -> request
                            .requestMatchers(HttpMethod.POST, ADMIN_PUBLIC_POST_ENDPOINTS).permitAll()
                            .anyRequest().authenticated())

            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                    .decoder(jwtDecoderConfig)
                    .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                    .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                    .bearerTokenResolver(cookieBearerTokenResolver))

            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    // ======================== Client Filter Chain ========================

    @Bean
    @Order(2)
    public SecurityFilterChain clientFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/v1/**", "/oauth2/**", "/login/oauth2/code/**")

            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(
                    request -> request
                            .requestMatchers(HttpMethod.POST, CLIENT_PUBLIC_POST_ENDPOINTS).permitAll()
                            .requestMatchers(HttpMethod.GET, CLIENT_PUBLIC_GET_ENDPOINTS).permitAll()
                            .anyRequest().authenticated())

            .oauth2Login(oauth2 -> oauth2
                    .authorizationEndpoint(endpoint -> endpoint
                            .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureHandler(oAuth2AuthenticationFailureHandler))

            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                    .decoder(jwtDecoderConfig)
                    .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                    .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                    .bearerTokenResolver(cookieBearerTokenResolver))

            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    // ======================== Authentication Managers ========================

    @Bean("adminAuthManager")
    public AuthenticationManager adminAuthManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(employeeUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Primary
    @Bean("clientAuthManager")
    public AuthenticationManager clientAuthManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customerUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    // ======================== Shared Beans ========================

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
