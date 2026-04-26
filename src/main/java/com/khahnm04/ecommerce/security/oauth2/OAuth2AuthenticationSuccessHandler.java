package com.khahnm04.ecommerce.security.oauth2;

import com.khahnm04.ecommerce.common.constant.TokenConstants;
import com.khahnm04.ecommerce.common.enums.AuthProvider;
import com.khahnm04.ecommerce.common.enums.CustomerStatus;
import com.khahnm04.ecommerce.common.enums.UserType;
import com.khahnm04.ecommerce.common.util.CookieUtils;
import com.khahnm04.ecommerce.dto.response.TokenPayload;
import com.khahnm04.ecommerce.entity.Customer;
import com.khahnm04.ecommerce.entity.UserProvider;
import com.khahnm04.ecommerce.repository.CustomerRepository;
import com.khahnm04.ecommerce.security.CookieProperties;
import com.khahnm04.ecommerce.security.JwtProperties;
import com.khahnm04.ecommerce.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final CookieProperties cookieProperties;
    private final JwtProperties jwtProperties;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        String targetUrl = redirectUri.orElse("http://localhost:3000/oauth2/redirect");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String avatar = oAuth2User.getAttribute("picture");
        String providerId = oAuth2User.getAttribute("sub");

        Customer customer = customerRepository.findByIdentifier(email)
                .orElseGet(() -> registerNewCustomer(email, name, avatar, providerId));

        if (customer.getUserProviders() == null || customer.getUserProviders().stream().noneMatch(p -> p.getProvider().equals(AuthProvider.GOOGLE))) {
            if (customer.getUserProviders() == null) {
                customer.setUserProviders(new HashSet<>());
            }
            customer.getUserProviders().add(UserProvider.builder()
                    .customer(customer)
                    .provider(AuthProvider.GOOGLE)
                    .providerId(providerId)
                    .build());
            customer = customerRepository.save(customer);
        }

        TokenPayload token = jwtService.generateToken(
                customer.getId(),
                email,
                "ROLE_CUSTOMER",
                UserType.CUSTOMER,
                false
        );

        addTokenCookies(response, token.token(), false);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .build().toUriString();
    }

    private Customer registerNewCustomer(String email, String name, String avatar, String providerId) {
        Customer customer = Customer.builder()
                .email(email)
                .fullName(name)
                .avatar(avatar)
                .phoneNumber("GG_" + UUID.randomUUID().toString().substring(0, 16))
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .status(CustomerStatus.ACTIVE)
                .isEmailVerified(true)
                .userProviders(new HashSet<>())
                .build();

        customer.getUserProviders().add(UserProvider.builder()
                .customer(customer)
                .provider(AuthProvider.GOOGLE)
                .providerId(providerId)
                .build());

        return customerRepository.save(customer);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private void addTokenCookies(HttpServletResponse response, String token, boolean rememberMe) {
        ResponseCookie refreshCookie = ResponseCookie.from(TokenConstants.TOKEN, token)
                .httpOnly(cookieProperties.isHttpOnly())
                .secure(cookieProperties.isSecure())
                .domain(cookieProperties.getDomain())
                .path("/")
                .maxAge(rememberMe
                        ? jwtProperties.getTokenRememberExpiration()
                        : jwtProperties.getTokenExpiration())
                .sameSite(cookieProperties.getSameSite())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

}
