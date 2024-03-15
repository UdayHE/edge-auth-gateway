package io.github.udayhe.edgeauthgateway.service;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author udayhegde
 */
public interface AuthenticationService {

    Mono<Void> logout(ServerWebExchange exchange, @AuthenticationPrincipal OidcUser principal);
}
