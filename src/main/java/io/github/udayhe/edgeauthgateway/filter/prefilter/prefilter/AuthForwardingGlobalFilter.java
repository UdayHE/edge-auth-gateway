package io.github.udayhe.edgeauthgateway.filter.prefilter.prefilter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static io.github.udayhe.edgeauthgateway.utils.Constants.*;
import static io.github.udayhe.edgeauthgateway.utils.JWTUtil.generateToken;
import static io.github.udayhe.edgeauthgateway.utils.TenantResolver.extractRegistrationIdFromURL;
import static org.apache.logging.log4j.util.Strings.isNotBlank;


/**
 *
 * @author udayhegde
 */

@Component
public class AuthForwardingGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(auth -> {
                    String registrationIdFromURL = extractRegistrationIdFromURL(exchange.getRequest().getURI());
                    if (auth.getPrincipal() instanceof OidcUser oidcUser) {
                        OidcUserInfo oidcUserInfo = oidcUser.getUserInfo();

                        if (oidcUserInfo != null) {
                            String jwtToken = generateToken(oidcUserInfo);
                            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                    .header(EDGE_GUARDIAN_TOKEN, jwtToken)
                                    .build();
                            setCookie(exchange, EDGE_GUARDIAN_TOKEN, jwtToken);
                            setCookie(exchange, OKTA_TENANT_ID, registrationIdFromURL);
                            return chain.filter(exchange.mutate().request(modifiedRequest).build());
                        }
                    }
                    // If not OIDCUser or oidcUserInfo is null, continue the chain without modification
                    setCookie(exchange, OKTA_TENANT_ID, registrationIdFromURL);
                    return chain.filter(exchange);
                });
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private void setCookie(ServerWebExchange exchange,
                           String cookieKey,
                           String cookieValue) {
        if(isNotBlank(cookieValue))
            exchange.getResponse().getCookies().add(cookieKey,
                    ResponseCookie.from(cookieKey, cookieValue).path(ROOT_PATH)
                            .httpOnly(false).secure(true).build());
    }
}
