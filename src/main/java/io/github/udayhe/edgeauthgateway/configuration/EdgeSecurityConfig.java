package io.github.udayhe.edgeauthgateway.configuration;

import io.github.udayhe.edgeauthgateway.matcher.BypassSecurityMatcher;
import io.github.udayhe.edgeauthgateway.oidc.CustomAuthenticationEntryPoint;
import io.github.udayhe.edgeauthgateway.oidc.CustomAuthenticationSuccessHandler;
import io.github.udayhe.edgeauthgateway.oidc.CustomAuthorizationRequestResolver;
import io.github.udayhe.edgeauthgateway.oidc.CustomLogoutSuccessHandler;
import io.github.udayhe.edgeauthgateway.repository.impl.TenantAwareClientRegistrationRepository;
import io.github.udayhe.edgeauthgateway.utils.JWTUtil;
import io.github.udayhe.edgeauthgateway.webfilter.RememberMeJwtAuthenticationFilter;
import io.github.udayhe.edgeauthgateway.webfilter.TenantIdentificationWebFilter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.web.server.SecurityWebFilterChain;


/**
 *
 * @author udayhegde
 */

@Configuration
@EnableWebFluxSecurity
@Slf4j
@AllArgsConstructor
public class EdgeSecurityConfig {

    private final TenantAwareClientRegistrationRepository tenantAwareClientRegistrationRepository;
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            ReactiveClientRegistrationRepository clientRegistrationRepository,
                                                            JWTUtil jwtUtil) {

        ServerOAuth2AuthorizationRequestResolver defaultAuthorizationRequestResolver =
                new DefaultServerOAuth2AuthorizationRequestResolver(clientRegistrationRepository);

        CustomAuthenticationEntryPoint customAuthenticationEntryPoint =
                new CustomAuthenticationEntryPoint(clientRegistrationRepository);

        ServerOAuth2AuthorizationRequestResolver customAuthorizationRequestResolver =
                new CustomAuthorizationRequestResolver(defaultAuthorizationRequestResolver);

        CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler =
                new CustomAuthenticationSuccessHandler();

        CustomLogoutSuccessHandler customLogoutSuccessHandler =
                new CustomLogoutSuccessHandler(clientRegistrationRepository);

        TenantIdentificationWebFilter tenantIdentificationWebFilter =
                new TenantIdentificationWebFilter();
        RememberMeJwtAuthenticationFilter rememberMeJwtAuthenticationFilter = new RememberMeJwtAuthenticationFilter(jwtUtil);

        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint))
                .addFilterBefore(tenantIdentificationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange(exchanges -> exchanges
                        .matchers(new BypassSecurityMatcher()).permitAll()
                        .anyExchange().authenticated())
                .oauth2Login(oauth2Login -> oauth2Login
                        .clientRegistrationRepository(tenantAwareClientRegistrationRepository)
                        .authorizationRequestResolver(customAuthorizationRequestResolver)
                        .authenticationSuccessHandler(customAuthenticationSuccessHandler)
                ).oidcLogout(oidcLogout -> {
                }).logout(logout -> logout
                .logoutUrl("/api/logout") // Keep your custom logout URL
                .logoutSuccessHandler(customLogoutSuccessHandler));

        return http.build();
    }
}

