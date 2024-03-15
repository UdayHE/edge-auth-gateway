package io.github.udayhe.edgeauthgateway.service.impl;

import io.github.udayhe.edgeauthgateway.dao.TenantConfig;
import io.github.udayhe.edgeauthgateway.service.AuthenticationService;
import io.github.udayhe.edgeauthgateway.service.TenantConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.session.data.redis.ReactiveRedisSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URLEncoder;

import static io.github.udayhe.edgeauthgateway.utils.NullSafeUtils.nullSafeGet;
import static io.github.udayhe.edgeauthgateway.utils.TenantResolver.extractRegistrationIdFromURL;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.logging.log4j.util.Strings.EMPTY;

/**
 * @author udayhegde
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final TenantConfigService tenantConfigService;
    private final ReactiveRedisSessionRepository sessionRepository;

    @Override
    public Mono<Void> logout(ServerWebExchange exchange, OidcUser principal) {
        String encodedIdTokenHint = URLEncoder.encode(principal.getIdToken().getTokenValue(), UTF_8);
        String registrationId = extractRegistrationIdFromURL(nullSafeGet(exchange,
                e -> e.getRequest().getURI(), null));
        TenantConfig tenantConfig = tenantConfigService
                .getTenantProperties(registrationId).orElse(null);
        return exchange.getSession()
                .flatMap(session -> {
                    String sessionId = session.getId();
                    session.invalidate();
                    return sessionRepository.deleteById(sessionId)
                            .then(Mono.just(sessionId));
                })
                .then(Mono.defer(() -> {
                    String logoutUrl = String.format("%s/oauth2/default/v1/logout?id_token_hint=%s",
                            nullSafeGet(tenantConfig, TenantConfig::getDomain, EMPTY), encodedIdTokenHint);
                    //https://mycompany.okta.com/oauth2/v1/logout?id_token_hint=%5Btoken%5D&post_logout_redirect_uri=http://localhost:3000/

                    log.info("Redirecting to logout URL: {}", logoutUrl);
                    exchange.getResponse().setStatusCode(HttpStatus.SEE_OTHER);
                    exchange.getResponse().getHeaders().setLocation(URI.create(logoutUrl));
                    return Mono.empty();
                }));
    }


}
