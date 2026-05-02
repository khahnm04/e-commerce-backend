package com.khahnm04.ecommerce.security.oauth2;

import com.khahnm04.ecommerce.common.constant.TokenConstants;
import com.khahnm04.ecommerce.common.enums.AuthProvider;
import com.khahnm04.ecommerce.common.enums.CustomerStatus;
import com.khahnm04.ecommerce.common.enums.UserType;
import com.khahnm04.ecommerce.common.util.CookieUtils;
import com.khahnm04.ecommerce.dto.response.TokenPayload;
import com.khahnm04.ecommerce.entity.Customer;
import com.khahnm04.ecommerce.entity.UserProvider;
import com.khahnm04.ecommerce.repository.user.CustomerRepository;
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
        String targetUrl = CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse("http://localhost:3000/oauth2/redirect");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String registrationId = request.getRequestURI().contains("github") ? "github" : "google";

        OAuth2UserInfo userInfo = OAuth2UserInfo.from(registrationId, oAuth2User);

        Customer customer = customerRepository.findByIdentifier(userInfo.resolvedEmail())
                .orElseGet(() -> registerNewCustomer(userInfo));

        linkProviderIfNeeded(customer, userInfo);

        TokenPayload token = jwtService.generateToken(
                customer.getId(),
                userInfo.resolvedEmail(),
                "ROLE_CUSTOMER",
                UserType.CUSTOMER,
                false
        );

        addTokenCookies(response, token.token(), false);

        return UriComponentsBuilder.fromUriString(targetUrl).build().toUriString();
    }

    // ======================== OAuth2 User Info ========================

    private record OAuth2UserInfo(AuthProvider provider, String email, String name, String avatar, String providerId) {

        static OAuth2UserInfo from(String registrationId, OAuth2User oAuth2User) {
            return switch (registrationId) {
                case "github" -> new OAuth2UserInfo(
                        AuthProvider.GITHUB,
                        oAuth2User.getAttribute("email"),
                        Optional.ofNullable((String) oAuth2User.getAttribute("name"))
                                .filter(n -> !n.isBlank())
                                .orElse(oAuth2User.getAttribute("login")),
                        oAuth2User.getAttribute("avatar_url"),
                        Optional.ofNullable(oAuth2User.getAttribute("id")).map(String::valueOf).orElse(null)
                );
                default -> new OAuth2UserInfo(
                        AuthProvider.GOOGLE,
                        oAuth2User.getAttribute("email"),
                        oAuth2User.getAttribute("name"),
                        oAuth2User.getAttribute("picture"),
                        oAuth2User.getAttribute("sub")
                );
            };
        }

        String resolvedEmail() {
            return (email != null && !email.isBlank())
                    ? email
                    : provider.name().toLowerCase() + "_" + providerId + "@placeholder.local";
        }
    }

    // ======================== Private methods ========================

    private void linkProviderIfNeeded(Customer customer, OAuth2UserInfo userInfo) {
        boolean alreadyLinked = customer.getUserProviders() != null
                && customer.getUserProviders().stream().anyMatch(p -> p.getProvider().equals(userInfo.provider()));

        if (alreadyLinked) return;

        if (customer.getUserProviders() == null) {
            customer.setUserProviders(new HashSet<>());
        }

        customer.getUserProviders().add(UserProvider.builder()
                .customer(customer)
                .provider(userInfo.provider())
                .providerId(userInfo.providerId())
                .build());
        customerRepository.save(customer);
    }

    private Customer registerNewCustomer(OAuth2UserInfo userInfo) {
        Customer customer = Customer.builder()
                .email(userInfo.resolvedEmail())
                .fullName(userInfo.name())
                .avatar(userInfo.avatar())
                .phoneNumber(userInfo.provider().name() + "_" + UUID.randomUUID().toString().substring(0, 16))
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .status(CustomerStatus.ACTIVE)
                .isEmailVerified(true)
                .userProviders(new HashSet<>())
                .build();

        customer.getUserProviders().add(UserProvider.builder()
                .customer(customer)
                .provider(userInfo.provider())
                .providerId(userInfo.providerId())
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
