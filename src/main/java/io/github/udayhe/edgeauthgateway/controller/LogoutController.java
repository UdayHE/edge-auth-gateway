package io.github.udayhe.edgeauthgateway.controller;

import io.github.udayhe.edgeauthgateway.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;



/**
 *
 * @author uday hegde
 */

@RestController
@Slf4j
@RequiredArgsConstructor
public class LogoutController {

    private final AuthenticationService authenticationService;

    @GetMapping("/api/logout")
    public Mono<Void> logout(ServerWebExchange exchange, @AuthenticationPrincipal OidcUser principal) {
        log.info("Initiating logout");
        return authenticationService.logout(exchange, principal);
    }

}
